package org.ranobe.ranobe.ui.reader.sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.ranobe.ranobe.App;
import org.ranobe.ranobe.config.Ranobe;
import org.ranobe.ranobe.databinding.SheetCustomizeReaderBinding;
import org.ranobe.ranobe.ui.reader.sheet.adapter.ReaderThemeAdapter;

public class CustomizeReader extends BottomSheetDialogFragment implements ReaderThemeAdapter.OnReaderThemeSelected {
    private final OnOptionSelection listener;
    private boolean isBionicRead;

    public CustomizeReader(OnOptionSelection listener,boolean isBionicRead) {
        this.listener = listener;
        this.isBionicRead = isBionicRead;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SheetCustomizeReaderBinding binding = SheetCustomizeReaderBinding.inflate(inflater, container, false);

        binding.fontSlider.setValue(Ranobe.getReaderFont(App.getContext()));
        binding.fontSlider.addOnChangeListener((slider, value, fromUser) -> listener.setFontSize(value));

        binding.readerThemeList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.readerThemeList.setAdapter(new ReaderThemeAdapter(this));

        binding.bionicReadingToggle.setChecked(isBionicRead);
        binding.bionicReadingToggle.setOnCheckedChangeListener((compoundButton, b) -> listener.setBionicRead(b));
        return binding.getRoot();
    }

    @Override
    public void select(String themeName) {
        listener.setReaderTheme(themeName);
    }

    public interface OnOptionSelection {
        void setFontSize(float size);

        void setReaderTheme(String themeName);

        void setBionicRead(boolean bionicReading);
    }
}
