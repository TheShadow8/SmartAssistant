package com.example.thanh.mobilefinal;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodEditFragment extends Fragment {
    private ArrayList<Map> foodList = new ArrayList();
    ListView listView;
    FoodAdapter foodAdapter;


    public FoodEditFragment() {
        // Required empty public constructor
    }

    public void init(ArrayList<Map> foodList) {

        this.foodList = foodList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_food_edit, container, false);
        listView = fragView.findViewById(R.id.ListView_food);

        foodAdapter = new FoodAdapter(getActivity());
        listView.setAdapter(foodAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> message = foodAdapter.getItem(position);
                long idInDb =  foodAdapter.getItemId(position);
                Bundle bundle = new Bundle();
                bundle.putLong("id",idInDb);
                bundle.putString("type", message.get(FoodDatabase.food_TYPE));
                bundle.putString("date",message.get(FoodDatabase.DATE));
                bundle.putString("time", message.get(FoodDatabase.TIME ));
                bundle.putString("calories", message.get("calories"));
                bundle.putString("total_Fat", message.get("total_Fat" ));
                bundle.putString("carbohydrate", message.get("carbohydrate"));
                Intent intent = new Intent(getActivity(), FoodEdit .class);
                intent.putExtra("bundle", bundle);
                getActivity().finish();
                startActivity(intent);
            }
        });

        return fragView;
    }

    private class FoodAdapter extends ArrayAdapter<Map<String, String>> {

        public FoodAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return foodList.size();
        }

        public Map<String, String> getItem(int position) {
            return foodList.get(position);
        }
        public long getItemId(int position) {
            String id = foodList.get(position).get("id").toString();
            return Long.parseLong(id);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View result = inflater.inflate(R.layout.food_item, null);
            if (!foodList.isEmpty()) {
                TextView message = result.findViewById(R.id.food_item_text);
                Map<String, String> food_view = getItem(position);

                String detail = "Food name: " + food_view.get("type") + " Date: " + food_view.get("date") + " Time: " + food_view.get("time") + " Calories: " + food_view.get("calories") + " Fat: " + food_view.get("total_Fat") + " Carbohydrate: " + food_view.get("carbohydrate");
                message.setText(detail);
            }
            return result;
        }
    }

}
