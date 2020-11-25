package com.example.resepque.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.resepque.R;
import com.example.resepque.activity.AddEditRecipeActivity;
import com.example.resepque.activity.ViewRecipeActivity;

public class AppBarFragment extends Fragment {
    public static final int MENU_NO_OPTIONS = 1;
    public static final int MENU_BACK_OPTION_EDIT = 2;
    public static final int MENU_BACK_OPTION_DELETE = 3;
    private final int layoutOption;
    LinearLayout menuWrapper;
    ImageView menu;
    ImageView option;

    public AppBarFragment(int layoutOption) {
        this.layoutOption = layoutOption;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_app_bar, container, false);
        menuWrapper = view.findViewById(R.id.menu_layout_wrapper);
        menu = view.findViewById(R.id.menu_snackbar);
        option = view.findViewById(R.id.menu_option);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupLayout();
    }

    private void setupLayout() {
        switch (layoutOption) {
            case MENU_NO_OPTIONS:
                menuWrapper.setOnClickListener(v -> Toast.makeText(getContext(), "app menu clicked", Toast.LENGTH_SHORT).show());
                option.setVisibility(View.INVISIBLE);
                break;
            case MENU_BACK_OPTION_EDIT:
                setBackMenu();
                option.setImageResource(R.drawable.ic_edit);
                if (getActivity() instanceof ViewRecipeActivity) {
                    menuWrapper.setOnClickListener(v -> ((ViewRecipeActivity) getActivity()).finish());
                    option.setOnClickListener(v -> ((ViewRecipeActivity) getActivity()).clickEditOption());
                }
                break;
            case MENU_BACK_OPTION_DELETE:
                setBackMenu();
                option.setImageResource(R.drawable.ic_delete);
                if (getActivity() instanceof AddEditRecipeActivity) {
                    menuWrapper.setOnClickListener(v -> ((AddEditRecipeActivity) getActivity()).popBack());
                    option.setOnClickListener(v -> ((AddEditRecipeActivity) getActivity()).removeThis());
                }
                break;
            default:
                break;
        }
    }

    private void setBackMenu() {
        menu.setImageResource(R.drawable.ic_arrow);
        menu.setScaleX(-1);
    }
}
