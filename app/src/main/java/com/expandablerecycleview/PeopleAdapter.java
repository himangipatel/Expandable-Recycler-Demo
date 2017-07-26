package com.expandablerecycleview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.expandablerecycleview.customview.ExpandableRecyclerAdapter;
import com.expandablerecycleview.model.Child;
import com.expandablerecycleview.model.Parent;

import java.util.ArrayList;
import java.util.List;

public class PeopleAdapter extends ExpandableRecyclerAdapter<PeopleAdapter.PeopleListItem> {
    public static final int TYPE_PERSON = 1001;

    public PeopleAdapter(Context context) {
        super(context);

        setItems(getSampleItems());
    }

    public static class PeopleListItem extends ExpandableRecyclerAdapter.ListItem {
        public String Text;


        public PeopleListItem(Parent group) {
            super(TYPE_HEADER);

            Text = group.getParentTitle();
        }

        public PeopleListItem(Child child) {
            super(TYPE_PERSON);

            Text = child.getfName() + " " + child.getlName();
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView name;

        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));

            name = (TextView) view.findViewById(R.id.item_header_name);
        }

        public void bind(int position) {
            super.bind(position);

            name.setText(visibleItems.get(position).Text);
        }
    }

    public class PersonViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        TextView name;

        public PersonViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.item_name);
        }

        public void bind(int position) {
            name.setText(visibleItems.get(position).Text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.item_header, parent));
            case TYPE_PERSON:
            default:
                return new PersonViewHolder(inflate(R.layout.item_person, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_PERSON:
            default:
                ((PersonViewHolder) holder).bind(position);
                break;
        }
    }

    private List<PeopleListItem> getSampleItems() {
        List<PeopleListItem> items = new ArrayList<>();

        items.add(new PeopleListItem(new Parent("Friends")));
        items.add(new PeopleListItem(new Child("Bill", "Smith")));
        items.add(new PeopleListItem(new Child("John", "Doe")));
        items.add(new PeopleListItem(new Child("Frank", "Hall")));
        items.add(new PeopleListItem(new Child("Sue", "West")));

        items.add(new PeopleListItem(new Parent("Family")));
        items.add(new PeopleListItem(new Child("Drew", "Smith")));
        items.add(new PeopleListItem(new Child("Chris", "Doe")));
        items.add(new PeopleListItem(new Child("Alex", "Hall")));

        items.add(new PeopleListItem(new Parent("Associates")));
        items.add(new PeopleListItem(new Child("John", "Jones")));
        items.add(new PeopleListItem(new Child("Ed", "Smith")));
        items.add(new PeopleListItem(new Child("Jane", "Hall")));
        items.add(new PeopleListItem(new Child("Tim", "Lake")));

        items.add(new PeopleListItem(new Parent("Colleagues")));
        items.add(new PeopleListItem(new Child("Carol", "Jones")));
        items.add(new PeopleListItem(new Child("Alex", "Smith")));
        items.add(new PeopleListItem(new Child("Kristin", "Hall")));
        items.add(new PeopleListItem(new Child("Pete", "Lake")));

        return items;
    }
}
