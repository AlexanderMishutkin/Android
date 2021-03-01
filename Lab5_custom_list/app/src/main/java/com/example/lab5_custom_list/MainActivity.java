package com.example.lab5_custom_list;

import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
    public class MyAdapter extends ArrayAdapter<String> {
        public MyAdapter(Context context, int resource, int
                textViewResourceId, String[] string) {
            super(context, resource, textViewResourceId, string);
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_item, parent,
                    false);
            String[] items =
                    getResources().getStringArray(R.array.images);
            ImageView image = (ImageView)
                    row.findViewById(R.id.imageView);
            TextView text = (TextView)
                    row.findViewById(R.id.textView2);
            text.setText(items[position]);

//            image.setImageResource(getResources().getIdentifier(items[position].toLowerCase().replace(" ", ""), "drawable", getPackageName()));
            switch (items[position]) {
                case "Image 1":
                    image.setImageResource(R.drawable.image1);
                    break;
                case "Image 2":
                    image.setImageResource(R.drawable.image2);
                    break;
                case "Image 3":
                    image.setImageResource(R.drawable.image3);
                    break;
                case "Image 4":
                    image.setImageResource(R.drawable.image4);
                    break;
            }

            return row;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setListAdapter(new MyAdapter(this,
                android.R.layout.simple_list_item_1, R.id.textView,
                getResources().getStringArray(R.array.images)));
    }
}

