package it.playfellas.superapp.ui.slave;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.playfellas.superapp.R;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class PhotoFragment extends Fragment {

    public static final String TAG = PhotoFragment.class.getSimpleName();
    private static final String MESSAGE = "Il gioco sta per iniziare";

    private static final int CAMERA_REQUEST = 1888;

    private Bitmap photo;
    private PhotoFragmentListener mListener;

    @Bind(R.id.photoImageView)
    public ImageView imageView;

    @Bind(R.id.takePhotoButton)
    public Button takePhotoButton;

    @Bind(R.id.continueButton)
    public Button continueButton;


    /**
     * Callback interface implemented in {@link SlaveActivity}
     */
    public interface PhotoFragmentListener {
        void setPhotoBitmap(Bitmap photo);

        void sendPhotoEvent();

        void recallWaitingFragment(String message);

        //TODO*********************************************
        //TODO ONLY FOR TESTING DURING DEVELOPMENT
        void selectSlaveGameFragment(int num);
        //TODO*********************************************
    }

    /**
     * Method to obtain a new Fragment's instance.
     *
     * @return This Fragment instance.
     */
    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }

    public PhotoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.photo_fragment, container, false);

        ButterKnife.bind(this, root);

        //TODO REMOVE BUTTON IN THE FINAL VERSION OF THIS APP
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PhotoFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + PhotoFragmentListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.continueButton)
    public void onClickContinueButton() {
        if (mListener != null) {
            if (photo == null) {
                //if photo is not available set a default photo
                photo = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.ic_launcher);
            }
            mListener.setPhotoBitmap(photo);
            //TODO FIX BECAUSE I MUST BE CONNECTED TO SEND PHOTO EVENT
            mListener.sendPhotoEvent();
            mListener.recallWaitingFragment(MESSAGE);
        }
    }


    //TODO *********************************************
    //TODO ONLY FOR TESTING DURING DEVELOPMENT
    @Bind(R.id.fragTestButton)
    public Button fragTestButton;
    @Bind(R.id.frag2TestButton)
    public Button frag2TestButton;
    @Bind(R.id.frag3TestButton)
    public Button frag3TestButton;

    @OnClick(R.id.fragTestButton)
    public void onClickFrag1Button() {
        if (mListener != null) {
            mListener.setPhotoBitmap(photo);
            mListener.sendPhotoEvent();
            mListener.selectSlaveGameFragment(1);
        }
    }

    @OnClick(R.id.frag2TestButton)
    public void onClickFrag2Button() {
        if (mListener != null) {
            mListener.setPhotoBitmap(photo);
            mListener.sendPhotoEvent();
            mListener.selectSlaveGameFragment(2);
        }
    }

    @OnClick(R.id.frag3TestButton)
    public void onClickFrag3Button() {
        if (mListener != null) {
            mListener.setPhotoBitmap(photo);
            mListener.sendPhotoEvent();
            mListener.selectSlaveGameFragment(3);
        }
    }
    //TODO *********************************************

}
