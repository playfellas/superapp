package it.playfellas.superapp.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.view.WindowManager;

import it.playfellas.superapp.R;

/**
 * Created by Stefano Cappa on 16/09/15.
 */

/**
 * Extends this class to create an ImmersiveDialogFragment, but remember to call showImmersive and not simply show().
 */
public class ImmersiveDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);

        // Temporarily set the dialogs window to not focusable to prevent the short
        // popup of the navigation bar.
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        return dialog;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setOnDismissListener(null);
        }
        super.onDestroyView();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //Here, I intentionally removed super.show(...) to prevent the normal dialogfragment behaviour
        this.showImmersive(manager, tag);
    }

    /**
     * This method is here to prevent problems related to this bug:
     * https://code.google.com/p/android/issues/detail?id=68031
     * based on the solution found here:
     * FOUND HERE: http://stackoverflow.com/questions/22794049/how-to-maintain-the-immersive-mode-in-dialogs
     */
    private void showImmersive(FragmentManager manager, String tag) {
        // Show the dialog.
        show(manager, tag);
        // It is necessary to call executePendingTransactions() on the FragmentManager
        // before hiding the navigation bar, because otherwise getWindow() would raise a
        // NullPointerException since the window was not yet created.
        getFragmentManager().executePendingTransactions();
        // Hide the navigation bar. It is important to do this after show() was called.
        // If we would do this in onCreateDialog(), we would get a requestFeature()
        // error.
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                getActivity().getWindow().getDecorView().getSystemUiVisibility()
        );
        // Make the dialogs window focusable again.
        getDialog().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        );
    }
}
