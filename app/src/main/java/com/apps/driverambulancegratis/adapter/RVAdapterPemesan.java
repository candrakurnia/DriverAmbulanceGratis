package com.apps.driverambulancegratis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.driverambulancegratis.R;
import com.apps.driverambulancegratis.model.User;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;

public class RVAdapterPemesan extends RecyclerView.Adapter<RVAdapterPemesan.ViewHolder> {

    private List<User> users;
//    private PesananActivity pesananActivity;
    private Context context;

    public RVAdapterPemesan(List<User> users, Context context) {
        this.users = users;
//        this.pesananActivity = pesananActivity;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_pemesan,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = users.get(position);
        holder.tvusername.setText(user.getFullname());
        holder.tvtelp.setText(user.getNo_telpon());
        holder.tvLng.setText(user.getAlamat());
        holder.selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "sudah diklik", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

//    public interface PesananActivity {
//        void pesananActivity(User user);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvusername,tvtelp,tvLng;
        MaterialButton selesai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvusername = itemView.findViewById(R.id.textnama);
            tvtelp = itemView.findViewById(R.id.tv_telpon);
            tvLng = itemView.findViewById(R.id.textktp);
            selesai = itemView.findViewById(R.id.btn_selesai);

        }
    }
}
