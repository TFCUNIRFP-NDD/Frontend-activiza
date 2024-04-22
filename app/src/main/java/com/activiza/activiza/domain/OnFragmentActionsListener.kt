package com.activiza.activiza.domain

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface OnFragmentActionsListener {
    fun setFragmentManager(manager: FragmentManager)
    fun onClickChangeFragments(fragment: Fragment)
}