    package com.example.androidcrudapplication2;


    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.List;

    public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder>{

        List<ModelClass> modelClassList;
        Context context;
        Database database;

        public EmployeeAdapter(List<ModelClass> modelClassList, Context context) {
            this.modelClassList = modelClassList;
            this.context = context;
            database = new Database(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitems,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final ModelClass modelClass = modelClassList.get(position);
            holder.id.setText(Integer.toString(modelClass.getId()));
            holder.name.setText(modelClass.getName());
            holder.email.setText(modelClass.getEmail());
            holder.contact.setText(modelClass.getContact());

            holder.edit.setOnClickListener(v->{
                String editName = holder.name.getText().toString();
                String editEmail = holder.email.getText().toString();
                String editContact = holder.contact.getText().toString();

                if(editName.length()>0 || editEmail.length()>0 || editContact.length()>0)
                {
                    database.updateEmployee(new ModelClass(modelClass.getId(),editName,editEmail,editContact));
                    Toast.makeText(context, "Data updated Successfully", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(context, "Enter data", Toast.LENGTH_SHORT).show();
                }



            });


            holder.delete.setOnClickListener(v->{

                database.deleteEmployee(modelClass.getId());
                modelClassList.remove(position);
                notifyDataSetChanged();

            });

        }

        @Override
        public int getItemCount() {
            return modelClassList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView id;
            EditText name,email,contact;
            Button edit,delete;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                id = itemView.findViewById(R.id.textView3);
                name = itemView.findViewById(R.id.editTextTextPersonName4);
                email = itemView.findViewById(R.id.editTextTextPersonName5);
                contact = itemView.findViewById(R.id.editTextTextPersonName6);
                edit = itemView.findViewById(R.id.edit);
                delete = itemView.findViewById(R.id.delete);

            }
        }
    }
