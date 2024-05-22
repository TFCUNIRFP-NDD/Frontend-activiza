package com.activiza.activiza.ui.view.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.activiza.activiza.R
import com.activiza.activiza.data.Message
import com.activiza.activiza.databinding.FragmentFeelsBinding
import com.activiza.activiza.ui.viewmodel.ChatAdapter
import com.activiza.activiza.ui.viewmodel.ChatViewModel


class FeelsFragment : Fragment() {
    private var _binding: FragmentFeelsBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFeelsBinding.inflate(inflater, container, false)
        val rootView = binding.root
        initUI()
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = layoutManager
        chatAdapter = ChatAdapter(requireContext(),emptyList())
        binding.recyclerView.adapter = chatAdapter

        chatViewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
            chatAdapter.updateMessages(messages)
            binding.recyclerView.scrollToPosition(messages.size - 1)
        })

        chatViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        })

        binding.sendButton.setOnClickListener {
            val messageContent = binding.messageEditText.text.toString()
            if (messageContent.isNotEmpty()) {
                val message = Message(
                    id = 0,
                    autor = "current_user", // Change this as necessary
                    titulo = null,
                    mensaje = messageContent,
                    media = null
                )
                chatViewModel.sendMessage(message)
                binding.messageEditText.text.clear()
            }
        }
    }

    private fun initUI() {

    }

}