package com.example.sean.termproject.Interfaces;

import com.example.sean.termproject.SwatchHelper.SwatchHelper;

public interface FragmentCallback {
    public void startExplorer();
    public void selectHue( SwatchHelper swatch );
    public void selectSaturation( SwatchHelper swatch );
    public void selectValue( SwatchHelper swatch );
    public SwatchHelper getSwatch();
    public void restartExplorer();
}