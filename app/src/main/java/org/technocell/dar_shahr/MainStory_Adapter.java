package org.technocell.dar_shahr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainStory_Adapter extends RecyclerView.Adapter<MainStory_Adapter.MainStoryViewHolder> {
    private Context mCtx;
    MainStroy_Items items;
    private List<MainStroy_Items> MainStoryItems;

    public MainStory_Adapter(Context mCtx,List<MainStroy_Items> MainStoryItems ){
        this.mCtx = mCtx;
        this.MainStoryItems = MainStoryItems;
    }

    @NonNull
    @Override
    public MainStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.story,parent,false);
        return new MainStoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainStoryViewHolder holder, int position) {
        items = MainStoryItems.get(position);
        Picasso.get().load(items.getPicture()).into(holder.Story_Image);
    }


    @Override
    public int getItemCount() {
        return MainStoryItems.size();
    }


    class MainStoryViewHolder extends RecyclerView.ViewHolder{
    CircleImageView Story_Image;
    public MainStoryViewHolder(@NonNull View itemView) {
        super(itemView);
        Story_Image = itemView.findViewById(R.id.Story_Image);
    }
}
}
