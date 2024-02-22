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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ModuleInfoListActivity extends AppCompatActivity {

    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_info_list);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.moduleinfolist_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext()));
        recyclerView.setAdapter(new ModuleInfoListActivity.ModuleInfoListRecyclerViewAdapter());

        Button addSensor = (Button)findViewById(R.id.addSensor);

        addSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addClick(view);
            }
        });
    }

    public void addClick(View view){
        Intent intent = new Intent(view.getContext(), AddSensorActivity.class);
        intent.putExtra("sendModuleName",getIntent().getStringExtra("sendModuleName"));
        startActivity(intent);
    }


    class ModuleInfoListRecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public List<ModuleModel> moduleModels;

        public ModuleInfoListRecyclerViewAdapter(){
            moduleModels = new ArrayList<>();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                uid = user.getUid();
                // Send token to your backend via HTTPS
                // 파이어베이스 DB에서 정보 불러오기.
                FirebaseDatabase.getInstance().getReference().child("users").child("labcas").child("module")
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        moduleModels.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.getValue(ModuleModel.class).moduleName.equals(getIntent().getStringExtra("sendModuleName"))){
                                moduleModels.add(snapshot.getValue(ModuleModel.class));
                            }
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module_info, parent,false);
            return new ModuleInfoListActivity.ModuleInfoListRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            double Tresult = moduleModels.get(position).T;
            double result = Math.round(Tresult);

            double Tresult2 = moduleModels.get(position).T2;
            double result2 = Math.round(Tresult2);

            double V_result = moduleModels.get(position).V;
            double V_result_1 = Math.round(V_result);


            double V2_result = moduleModels.get(position).V2;
            double V2_result_1 = Math.round(V2_result);

            ((CustomViewHolder)holder).moduleName.setText("이름 : " + moduleModels.get(position).moduleName );
            ((CustomViewHolder)holder).modulestatus.setText("상태 : " + moduleModels.get(position).status );
            ((CustomViewHolder)holder).moduleTemp1.setText("온도 1 : "+result+"°C");
            ((CustomViewHolder)holder).moduleTemp2.setText("온도 2 : "+result2 +"°C");
            ((CustomViewHolder)holder).moduleVolt1.setText("전압 1 : "+V_result_1+"V");
            ((CustomViewHolder)holder).moduleVolt2.setText("전압 2 :" + V2_result_1+"V");
        }

        @Override
        public int getItemCount() {
            return moduleModels.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder{

            public TextView moduleName;
            public TextView modulestatus;

            public TextView moduleTemp1;

            public TextView moduleTemp2;

            public TextView moduleVolt1;

            public TextView moduleVolt2;

            public CustomViewHolder(View view){
                super(view);

                moduleName = (TextView)view.findViewById(R.id.moduleInfo_text_moduleName);
                modulestatus = (TextView)view.findViewById(R.id.moduleInfo_text_status);
                moduleTemp1 = (TextView)view.findViewById(R.id.moduleInfo_text_temp1);
                moduleTemp2 = (TextView)view.findViewById(R.id.moduleInfo_text_temp2);
                moduleVolt1 = (TextView)view.findViewById(R.id.moduleInfo_text_voltage1);
                moduleVolt2 = (TextView)view.findViewById(R.id.moduleInfo_text_voltage2);
            }
        }
    }
}