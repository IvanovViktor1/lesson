package com.example.bookapp2.ui.testEditing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookapp2.databinding.FragmentTesteditingBinding;

public class testEditingFragment extends Fragment {

    private FragmentTesteditingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        testEditingViewModel testEditingViewModel =
                new ViewModelProvider(this).get(testEditingViewModel.class);

        binding = FragmentTesteditingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textView7;
        testEditingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


                return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}