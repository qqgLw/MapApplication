package com.example.map_app.authentication

import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.map_app.R
import com.example.map_app.databinding.FooterVholderBinding
import com.example.map_app.databinding.ItemVholderBinding
import com.example.map_app.hideKeyboard
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch


class AuthRecViewAdapter(
    private val formDataset: List<String>,
    private val authViewModel: AuthViewModel,
    var formSwitchListener: ((aVM:AuthViewModel) -> Unit)? = null)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var switchState : Boolean = (formDataset[0]=="Вход")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            R.layout.header_vholder ->{
                val layoutInflater = LayoutInflater.from(parent.context)
                val v = layoutInflater.inflate(viewType, parent, false)
                HeaderViewHolder(v)
            }
            R.layout.item_vholder -> ItemViewHolder.from(parent)
            R.layout.footer_vholder -> FooterViewHolder.from(parent,
                parent.context as LifecycleOwner)
            else->{
                throw RuntimeException("No type to match type $viewType!")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind()
            is ItemViewHolder -> {
                holder.bind(position,formDataset[position],switchState,authViewModel)
            }
            is FooterViewHolder -> {
                holder.bind(formDataset[position],authViewModel,switchState)
            }
        }
    }

    override fun getItemCount() = formDataset.size

    override fun getItemViewType(position: Int): Int {
        return when (position){
            0 -> R.layout.header_vholder
            itemCount.dec() -> R.layout.footer_vholder
            else -> R.layout.item_vholder
        }
    }

    inner class HeaderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), ToggleSwitch.OnChangeListener
    {
        private val switchPosition = mapOf(true to 0, false to 1)

        private var switch: ToggleSwitch = itemView.findViewById(R.id.header_switch)

        override fun onToggleSwitchChanged(position: Int) {
            formSwitchListener?.invoke(authViewModel)
        }

        fun bind() {
            switch.setCheckedPosition(switchPosition[switchState]!!)
            switch.onChangeListener = this
        }
    }

    class ItemViewHolder private constructor(private val binding: ItemVholderBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemVholderBinding.inflate(layoutInflater,parent,false)
                return ItemViewHolder(binding)
            }

            val formType = mapOf(true to "Login", false to "Register")
        }

        fun bind(
            position: Int,
            text: String,
            switchState: Boolean,
            authViewModel: AuthViewModel
        ) {
            val localTag : Pair<Int,String?> = Pair(position.dec(), formType[switchState])
            val inputText = authViewModel.getInputValue(position,switchState)

            binding.inputField.hint = text
            binding.inputField.tag = localTag
            binding.inputField.setText(inputText)

            binding.authViewModel=authViewModel
            binding.inputField.setOnEditorActionListener(OnEditorActionFooterHandler(switchState))
            regInputTypeSet(switchState, position)
        }

        private fun regInputTypeSet(
            switchState:Boolean,
            position: Int,
        ) {
            if (switchState) {
                if (position == 2) {
                    binding.inputContainer.isPasswordVisibilityToggleEnabled = true
                    binding.inputField.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.inputField.imeOptions=EditorInfo.IME_ACTION_DONE
                }
            } else {
                when (position) {
                    2 -> binding.inputField.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    3 ->{
                        binding.inputField.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        binding.inputContainer.isPasswordVisibilityToggleEnabled = true}
                    4 -> {
                        binding.inputField.imeOptions=EditorInfo.IME_ACTION_DONE
                        binding.inputContainer.isPasswordVisibilityToggleEnabled = true
                        binding.inputField.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
            }
        }
        inner class OnEditorActionFooterHandler(val mSwitchState: Boolean) : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.root.hideKeyboard()
                    //необходимо сделать .performClick() у кнопки футера
                    //binding.authViewModel?.onFormSubmit(mSwitchState)
                    return true
                }
                return false
            }
        }
    }

    class FooterViewHolder private constructor(private val binding: FooterVholderBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun from(parent: ViewGroup, lifecycleOwner: LifecycleOwner): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FooterVholderBinding.inflate(layoutInflater,parent,false)
                binding.lifecycleOwner = lifecycleOwner
                return FooterViewHolder(binding)
            }
        }

        fun bind(
            text: String,
            authViewModel: AuthViewModel,
            switchState: Boolean
        ) {
            binding.authViewModel=authViewModel
            binding.formState=switchState
            binding.footerButton.text = text
            binding.termsOfUseSwitch.isChecked=switchState
        }
    }
}