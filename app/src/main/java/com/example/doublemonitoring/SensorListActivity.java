package com.example.doublemonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.doublemonitoring.model.ModuleModel;
import com.example.doublemonitoring.model.SensorModel;
import com.example.doublemonitoring.model.VoltModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SensorListActivity extends AppCompatActivity {

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.sensorList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext()));
        recyclerView.setAdapter(new SensorListActivity.SensorListRecyclerViewAdapter());

        Button addSensor = (Button)findViewById(R.id.sensorlist_addSensor);

        addSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), AddSensorActivity.class);
                startActivity(intent);
            }
        });
    }


    class SensorListRecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public List<SensorModel> sensorModels;

        public SensorListRecyclerViewAdapter(){
            sensorModels = new ArrayList<>();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                uid = user.getUid();

                // 파이어베이스 DB에서 정보 불러오기.
                FirebaseDatabase.getInstance().getReference().child("users").child("test").child("temp")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                sensorModels.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    sensorModels.add(snapshot.getValue(SensorModel.class));
                                }
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                // 파이어베이스 DB에서 정보 불러오기.
                FirebaseDatabase.getInstance().getReference().child("users").child("test").child("volt")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                sensorModels.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    sensorModels.add(snapshot.getValue(SensorModel.class));
                                }
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }




        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sensor, parent,false);
            return new SensorListActivity.SensorListRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


            //((SensorListActivity.SensorListRecyclerViewAdapter.CustomViewHolder)holder).sensorName.setText("센서 이름 : " + sensorModels.get(position).sensorName );
            ((SensorListActivity.SensorListRecyclerViewAdapter.CustomViewHolder)holder).sensorAddress.setText("센서 주소값 : " + sensorModels.get(position).sensorAddress);
            ((SensorListActivity.SensorListRecyclerViewAdapter.CustomViewHolder)holder).sensorValue.setText("센서 측정 값 : " + sensorModels.get(position).sensorValue);

            if(sensorModels.get(position).sensorType.equals("T")){
                ((SensorListActivity.SensorListRecyclerViewAdapter.CustomViewHolder)holder).sensorType.setText("센서 종류 : 온도 센서");


            }


            else if(sensorModels.get(position).sensorType.equals("V")){
                ((SensorListActivity.SensorListRecyclerViewAdapter.CustomViewHolder)holder).sensorType.setText("센서 종류 : 전압 센서");
            }
        }

        @Override
        public int getItemCount() {
            return sensorModels.size();
        }


        class CustomViewHolder extends RecyclerView.ViewHolder{

            public TextView sensorName;

            public TextView sensorType;

            public TextView sensorAddress;

            public TextView sensorValue;

            public CustomViewHolder(View view){
                super(view);
                sensorName = (TextView)view.findViewById(R.id.sensor_name);
                sensorType = (TextView)view.findViewById(R.id.sensor_type);
                sensorAddress = (TextView)view.findViewById(R.id.sensor_address);
                sensorValue = (TextView)view.findViewById(R.id.sensor_value);
            }
        }
    }

}