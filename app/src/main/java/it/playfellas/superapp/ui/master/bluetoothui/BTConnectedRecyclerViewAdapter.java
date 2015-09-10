package it.playfellas.superapp.ui.master.bluetoothui;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;
import lombok.Getter;

public class BTConnectedRecyclerViewAdapter extends RecyclerView.Adapter<BTConnectedRecyclerViewAdapter.ViewHolder> {
    private ItemClickListener itemClickListener;

    @Getter
    private List<BluetoothDevice> connectedDevices;

    public BTConnectedRecyclerViewAdapter(@NonNull ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        connectedDevices = new ArrayList<>();
        setHasStableIds(true);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View parent;

        @Bind(R.id.connectedDeviceImageView)
        ImageView connectedDeviceImageView;
        @Bind(R.id.nameTextView)
        TextView nameTextView;
        @Bind(R.id.addressTextView)
        TextView addressTextView;
        @Bind(R.id.disconnectButton)
        Button disconnectButton;

        public ViewHolder(View view) {
            super(view);
            this.parent = view;
            ButterKnife.bind(this, view);
        }

        public void setOnClickListener(OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.connected_device_row, viewGroup, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final BluetoothDevice device = this.connectedDevices.get(position);

        viewHolder.nameTextView.setText(device.getName());
        viewHolder.addressTextView.setText(device.getAddress());
        viewHolder.disconnectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.disconnectButtonClicked(device);
            }
        });

        viewHolder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClicked(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.connectedDevices.size();
    }

    public interface ItemClickListener {
        void itemClicked(final View view);

        void disconnectButtonClicked(BluetoothDevice deviceToDisconnect);
    }
}