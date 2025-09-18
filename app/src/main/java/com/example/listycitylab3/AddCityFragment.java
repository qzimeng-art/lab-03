package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
        void setCity(int position, City city);
    }

    private AddCityDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        boolean editMode = false;
        int position = -1;
        City city = null;

        // when adding, bundle is null, thus containsKey pointing to Null will fail
        Bundle args = getArguments();
        if (args != null && args.containsKey("city")) {
            Object serial = args.getSerializable("city");
            if (serial instanceof City) {
                city = (City) serial;
                position = args.getInt("position");
                editMode = true;
                if (city.getName() != null && city.getProvince() != null) {
                    editCityName.setText(city.getName());
                    editProvinceName.setText(city.getProvince());
                }
            }
        }

        String type = editMode ? "Edit a city" : "Add a city";
        String operation = editMode ? "Modify" : "Add";

        // mandatory final attribute in lambda expression
        final boolean finalEditMode = editMode;
        final int finalPosition = position;

        return builder
                .setView(view)
                .setTitle(type)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(operation, (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if (!finalEditMode) {
                        listener.addCity(new City(cityName, provinceName));
                    } else {
                        listener.setCity(finalPosition, new City(cityName, provinceName));
                    }
                })
                .create();
    }

    static AddCityFragment newInstance (City city, int position) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        args.putInt("position", position);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
