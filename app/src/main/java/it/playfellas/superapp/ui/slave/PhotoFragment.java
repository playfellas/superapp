package it.playfellas.superapp.ui.slave;

import android.content.Context;
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
    ImageView imageView;

    @Bind(R.id.takePhotoButton)
    Button takePhotoButton;

    @Bind(R.id.continueButton)
    Button continueButton;


    /**
     * Callback interface implemented in {@link SlaveActivity}
     */
    public interface PhotoFragmentListener {
        void setPhotoBitmap(Bitmap photo);

        void sendPhotoEvent();

        void recallWaitingFragment(String message);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (PhotoFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
