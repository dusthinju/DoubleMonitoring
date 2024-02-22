package com.example.doublemonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class ModuleListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ModuleListActivity.ModuleListRecyclerViewAdapter adapter;
    String uid;

    Button addModuleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_list);


        RecyclerView recyclerView = findViewById(R.id.modulelist_recyclerView);

        firebaseAuth = FirebaseAuth.getInstance();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ModuleListActivity.ModuleListRecyclerViewAdapter();

        recyclerView.setAdapter(adapter);

        addModuleBtn = (Button) findViewById(R.id.addModuleBtn);
        addModuleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddModuleActivity.class);
                startActivity(intent);
            }
        });
    }

    class ModuleListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        
        List<ModuleModel> moduleModels;

        ModuleListRecyclerViewAdapter() {

            //현재 로그인 된 firebase user 정보 불러오기
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();

            moduleModels = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("users").child("labcas").child("module")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            moduleModels.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                moduleModels.add(snapshot.getValue(ModuleModel.class));
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_module_grid, viewGroup, false);

            return new ModuleListActivity.ModuleListRecyclerViewAdapter.CustomViewHolder(view);
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


            ((ModuleListActivity.ModuleListRecyclerViewAdapter.CustomViewHolder) holder).module_name.setText(moduleModels.get(position).moduleName);
            ((ModuleListActivity.ModuleListRecyclerViewAdapter.CustomViewHolder) holder).module_temp1.setText("온도 1 : " + result+"°C");
            ((ModuleListActivity.ModuleListRecyclerViewAdapter.CustomViewHolder) holder).module_temp2.setText("온도 2 : " + result2+"°C");
            ((ModuleListActivity.ModuleListRecyclerViewAdapter.CustomViewHolder) holder).module_volt1.setText("전압 1 : " + V_result_1 +"V");
            ((ModuleListActivity.ModuleListRecyclerViewAdapter.CustomViewHolder) holder).module_volt2.setText("전압 2 : " + V2_result_1+"V");
            String moduleName = moduleModels.get(position).moduleName;

            // 아이템을 클릭시 모듈정보를 담은 페이지로 넘어간다.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ModuleInfoListActivity.class);
                    intent.putExtra("sendModuleName",moduleModels.get(position).moduleName);
                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return moduleModels.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView module_temp1;

            public TextView module_temp2;

            public TextView module_volt1;
            public TextView module_volt2;
            public TextView module_name;
            public TextView module_status;

            public CustomViewHolder(View view) {
                super(view);
                module_temp1 = (TextView) view.findViewById(R.id.moduleItem_textview_temp1);
                module_temp2 = (TextView) view.findViewById(R.id.moduleItem_textview_temp2);
                module_volt1 = (TextView) view.findViewById(R.id.moduleItem_textview_volt1);
                module_volt2 = (TextView) view.findViewById(R.id.moduleItem_textview_volt2);
                module_name = (TextView) view.findViewById(R.id.moduleItem_textView_name);

            }

        }
    }
}