package com.activiza.activiza.ui.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.activiza.activiza.R
import com.activiza.activiza.domain.OnFragmentActionsListener

class FragmentFunctions: OnFragmentActionsListener{

    private lateinit var fragmentManager: FragmentManager

    override fun setFragmentManager(manager: FragmentManager) {
        this.fragmentManager = manager
    }

    override fun onClickChangeFragments(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navHostFragment, fragment)
        fragmentTransaction.commit()
    }

}