package it.playfellas.superapp.activities.slave;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

    private static final int CAMERA_REQUEST = 1888;

    @Bind(R.id.photoImageView)
    public ImageView imageView;

    @Bind(R.id.takePhotoButton)
    public Button takePhotoButton;

    @Bind(R.id.continueButton)
    public Button continueButton;

    private PhotoFragmentListener mListener;


    /**
     * Callback interface implemented in {@link SlaveActivity}
     */
    public interface PhotoFragmentListener {
        void selectWaitingFragment();
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
        View root = inflater.inflate(R.layout.fragment_slave, container, false);

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
            Bitmap photo = (Bitmap) data.getExtras().get("data");
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
            mListener.selectWaitingFragment();
        }
    }
}
