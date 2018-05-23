package com.saluchen.thisproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RequestHistoryAdapter extends RecyclerView.Adapter<RequestHistoryAdapter.MyViewHolder> {

    private Context mContext;
    List<RequestHistoryItem> requestHistoryItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public TextView itemDetail;
        public TextView expectedDateTime;
        public TextView deliveryLocation;
        public TextView status;

        public MyViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.request_card_item_name);
            itemDetail = view.findViewById(R.id.request_card_item_details);
            expectedDateTime = view.findViewById(R.id.request_card_datetime);
            deliveryLocation = view.findViewById(R.id.request_card_delivery_location);
            status = view.findViewById(R.id.request_card_status);
        }
    }

    public RequestHistoryAdapter(Context mContext, List<RequestHistoryItem> requestHistoryItemList) {
        this.mContext = mContext;
        this.requestHistoryItemList = requestHistoryItemList;
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_history_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RequestHistoryItem requestHistoryItem = requestHistoryItemList.get(position);
        final String itemName = requestHistoryItem.getItemName();
        final String itemDetail = requestHistoryItem.getItemDetails();
        final String expectedDateTime = requestHistoryItem.getExpectedDatetime();
        final String deliveryLocation = requestHistoryItem.getDeliveryLocation();
        final String status = requestHistoryItem.getStatus();

        holder.itemName.setText(itemName);
        holder.itemDetail.setText(itemDetail);
        holder.expectedDateTime.setText(expectedDateTime);
        holder.deliveryLocation.setText(deliveryLocation);
        holder.status.setText(status);
    }

    @Override
    public int getItemCount() {
        return requestHistoryItemList.size();
    }
}
