package it.playfellas.superapp.ui.master;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.playfellas.superapp.R;

public class BTConnectedRecyclerViewAdapter extends RecyclerView.Adapter<BTConnectedRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private ItemClickListener itemClickListener;

    public BTConnectedRecyclerViewAdapter(Context context, @NonNull ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        setHasStableIds(true);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private final View parent;

        @Bind(R.id.nameTextView)
        TextView nameTextView;
        @Bind(R.id.addressTextView)
        TextView addressTextView;

        public ViewHolder(View view, Context context) {
            super(view);
            this.context = context;
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
        return new ViewHolder(v, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        BluetoothDevice device = BTConnectedDevices.get().getConnectedDevices().get(position);

        viewHolder.nameTextView.setText(device.getName());
        viewHolder.addressTextView.setText(device.getAddress());

        viewHolder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClicked(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return BTConnectedDevices.get().getConnectedDevices().size();
    }

    public interface ItemClickListener {
        void itemClicked(final View view);
    }
}