package it.playfellas.superapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager;

import it.playfellas.superapp.R;

/**
 * Created by Stefano Cappa on 16/09/15.
 */
public class ImmersiveDialogFragment extends DialogFragment {

    /**
     * Extends this class to create an ImmersiveDialogFragment, but remember to call this method with super.setImmersive()
     * in the onCreateView of the subclass.
     * Also, you should call super.setImmersive() in the onCreateView as last code-line before the return.
     * <p/>
     * Created using http://stackoverflow.com/questions/22794049/how-to-maintain-the-immersive-mode-in-dialogs
     */
    public void setImmersive() {
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        getDialog().getWindow().getDecorView().setSystemUiVisibility(getActivity().getWindow().getDecorView().getSystemUiVisibility());

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //Clear the not focusable flag from the window
                getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                //Update the WindowManager with the new attributes (no nicer way I know of to do this)..
                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                wm.updateViewLayout(getDialog().getWindow().getDecorView(), getDialog().getWindow().getAttributes());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }
}
