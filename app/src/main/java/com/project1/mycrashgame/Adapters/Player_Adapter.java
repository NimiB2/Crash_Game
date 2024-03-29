package com.project1.mycrashgame.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.project1.mycrashgame.Interfaces.Callback_recordClicked;
import com.project1.mycrashgame.Model.Player;
import com.project1.mycrashgame.R;

import java.util.ArrayList;

public class Player_Adapter extends RecyclerView.Adapter<Player_Adapter.PlayerViewHolder> {
    private ArrayList<Player> players;
    private Context context;
    private Callback_recordClicked callbackRecordClicked;

    public Player_Adapter(Callback_recordClicked callbackRecordClicked, ArrayList<Player> players, Context context) {
        this.players = players;
        this.callbackRecordClicked=callbackRecordClicked;
        this.context = context;
    }


    @NonNull
    @Override
    public Player_Adapter.PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_record, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Player_Adapter.PlayerViewHolder holder, int position) {
        Player player = getPlayer(position);
        holder.player_MTV_index.setText(String.valueOf(position+1));
        holder.player_MTV_name.setText(player.getName());
        holder.player_MTV_score.setText(String.valueOf(player.getScore()));

    }
    public void setCallback(Callback_recordClicked callbackRecordClicked) {
       this.callbackRecordClicked= callbackRecordClicked;
    }

    @Override
    public int getItemCount() {
        return players == null ? 0 : players.size();
    }

    public void setData(ArrayList<Player> players) {
        this.players = players;
    }

    private Player getPlayer(int position){
       return players.get(position);
    }
    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView player_MTV_index;
        private MaterialTextView player_MTV_name;
        private MaterialTextView player_MTV_score;
        private ShapeableImageView player_IMG_playerMap;


        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            player_MTV_index = itemView.findViewById(R.id.player_MTV_index);
            player_MTV_name = itemView.findViewById(R.id.player_MTV_name);
            player_MTV_score = itemView.findViewById(R.id.player_MTV_score);
            player_IMG_playerMap = itemView.findViewById(R.id.player_IMG_playerMap);

            player_IMG_playerMap.setOnClickListener(v->
            {
                if (callbackRecordClicked != null) {
                    Player p = getPlayer(getAdapterPosition());
                    callbackRecordClicked.getRecordMap(p.getLat(), p.getLon(), p.getName());
                }
            });
        }

    }
}
