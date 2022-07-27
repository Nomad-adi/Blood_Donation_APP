package com.example.blooddonationapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blooddonationapp.R;
import com.example.blooddonationapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.user_adapter_layout,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = userList.get(position);
        holder.userType.setText(user.getType());
        if(user.getType()!= null && user.getType().equals("donor")){
            holder.emailNowBtn.setVisibility(View.VISIBLE);
        }


        holder.userName.setText(user.getName());
        holder.user_blood_group.setText(user.getBloodGroups());
        holder.userEmail.setText(user.getEmail());
        holder.userPh.setText(user.getPhNo());
        if(user.getProfilePictureUrl()!=null){
            Glide.with(context).load(user.getProfilePictureUrl()).into(holder.userprofiel_image);
        }else{
            holder.userprofiel_image.setImageResource(R.drawable.user);
        }

        // send email
        final String nameOfReceiver = user.getName();
        final String idOfReceiver = user.getId();
        holder.emailNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("QueryPermissionsNeeded")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // sender details
                        String senderName = snapshot.child("name").getValue().toString();
                        String senderPhn = snapshot.child("phNo").getValue().toString();
                        String blood = snapshot.child("bloodGroups").getValue().toString();
                        String senderEmail = snapshot.child("email").getValue().toString();

                        // recevier details
                        String sendMailto = user.getEmail();
                        String subject = "Blood Donation";
                        String Emessage = "Hello "+nameOfReceiver+",\n"+"\n"+senderName+
                                            " would like blood donation from you. Here's his/her details\n"+
                                        "\nName:  "+senderName+"\n"+
                                        "Phone Number: "+senderPhn+"\n"+
                                        "Email: "+senderEmail+"\n"+
                                        "Blood Groups: "+blood+"\n"+
                                        "Kindly Reach out to him/her.\n  Thank You!\n"+
                                        "\nBlood Donation APP- Donate Blood Saves Lives!";

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{sendMailto});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
                        emailIntent.putExtra(Intent.EXTRA_TEXT,Emessage);
                        emailIntent.setData(Uri.parse("mailto:"));

                        context.startActivity(emailIntent);
                        if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(emailIntent);

                            // adding to database
                            DatabaseReference sender_ref = FirebaseDatabase.getInstance().getReference("emails").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            sender_ref.child(idOfReceiver).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        DatabaseReference receiver_ref = FirebaseDatabase.getInstance().getReference("emails")
                                                .child(idOfReceiver);
                                        receiver_ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                                        Log.d("email2","pass");
                                    }else {
                                        Log.d("email1","failed");
                                    }
                                }
                            });
                        }
//
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userprofiel_image;
        private TextView userName,userEmail,userPh,userType,user_blood_group;
        private Button emailNowBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userprofiel_image = itemView.findViewById(R.id.userprofile_image);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userPh = itemView.findViewById(R.id.userPh);
            userType = itemView.findViewById(R.id.userType);
            user_blood_group = itemView.findViewById(R.id.user_blood_group);
            emailNowBtn = itemView.findViewById(R.id.emailNowBtn);

        }
    }
}
