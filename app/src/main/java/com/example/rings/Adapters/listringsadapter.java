package com.example.rings.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rings.R;
import com.example.rings.model.myringmodel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class listringsadapter extends RecyclerView.Adapter<listringsadapter.MyViewHolder> {
    private List<myringmodel> mDataset;
    Context contxt;
    OnShareClickedListener mCallback;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView myringcardstatus, myringnamecard;
        ImageView imgmyringcard;
        ImageButton imgbtncardmore;
        MaterialCardView cardmyringsmenu;
        LinearLayout lnDel;
        View bckdel;
        ProgressBar pgbloadmyring;

        public MyViewHolder(MaterialCardView v) {
            super(v);
            myringcardstatus = v.findViewById(R.id.myringcardstatus);
            myringnamecard = v.findViewById(R.id.myringnamecard);
            imgmyringcard = v.findViewById(R.id.imgmyringcard);
            cardmyringsmenu = v.findViewById(R.id.cardmyringsmenu);
            imgbtncardmore = v.findViewById(R.id.imgbtncardmore);
            lnDel = v.findViewById(R.id.lnDel);
            bckdel = v.findViewById(R.id.bckdel);
            pgbloadmyring = v.findViewById(R.id.pgbloadmyring);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public listringsadapter(Context cxt,OnShareClickedListener ct, List<myringmodel> myDataset) {
        contxt = cxt;
        mCallback = ct;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public listringsadapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        MaterialCardView v =  (MaterialCardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myringcard, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView status = holder.myringcardstatus;
        TextView ringname = holder.myringnamecard;
        final ImageButton options = holder.imgbtncardmore;
        MaterialCardView card = holder.cardmyringsmenu;
        final View bckdel = holder.bckdel;
        final LinearLayout lnDel = holder.lnDel;

        status.setText("Idle");
        ringname.setText(mDataset.get(position).getRingname());

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.cardcliked(position);
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), holder.imgbtncardmore);
                popup.getMenuInflater().inflate(R.menu.myringoptions, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        mCallback.menuclicked(item,position, bckdel,lnDel,options, mDataset);
                        return true;
                    }
                });

                popup.show();
            }
        });

        dowloadimage(holder.imgmyringcard, position,holder.pgbloadmyring);

    }

    private void dowloadimage(final ImageView V, int pos, final ProgressBar pg){
        // Get a non-default Storage bucket
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ringdp");
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a reference to 'ringsprofilepicture/mountains.jpg'

        storageRef.child("rings_profile_picture/"+mDataset.get(pos).getRingname()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.get().load(uri).into(V);
                pg.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                pg.setVisibility(View.GONE);
                System.out.println("========download error occurred======"+exception.getMessage());
            }
        });

    }

    public void setOnShareClickedListener(OnShareClickedListener mCallback) {
        this.mCallback = mCallback;
    }

    public interface OnShareClickedListener {
        public void cardcliked(int position);
        public void menuclicked(MenuItem menuname, int position, View vw, LinearLayout ln,ImageButton options, List<myringmodel> mDataset);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
