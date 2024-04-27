package poly.manhnt.datn_md09.Adapters;
import  android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;

import android.graphics.drawable.BitmapDrawable;

import android.graphics.drawable.VectorDrawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import poly.manhnt.datn_md09.Models.HomeModel.MenuModel.MenuModel;
import poly.manhnt.datn_md09.Models.Objects.LoaiSanPham;
import poly.manhnt.datn_md09.R;

public class ExpandAdapter extends BaseExpandableListAdapter {
    Context context;
    List<LoaiSanPham> loaiSanPhamList;
    ViewHolderMenu viewHolderMenu;
    public ExpandAdapter(Context context, List<LoaiSanPham> loaiSanPhamList){
        this.context = context;
        this.loaiSanPhamList = loaiSanPhamList;

        MenuModel menuModel = new MenuModel();
        int count = loaiSanPhamList.size();
        for (int i=0; i<count; i++){
            int maLoaiSP = loaiSanPhamList.get(i).getMALOAISP();
            loaiSanPhamList.get(i).setListCon(menuModel.layLoaiSPtheoMaLoai(maLoaiSP));
        }

    }
    @Override
    public int getGroupCount() {
        return loaiSanPhamList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if(loaiSanPhamList.get(i).getListCon().size() != 0){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int i) {
        return loaiSanPhamList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return loaiSanPhamList.get(i).getListCon().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return loaiSanPhamList.get(i).getMALOAISP();
    }

    @Override
    public long getChildId(int i, int i1) {
        return loaiSanPhamList.get(i).getListCon().get(i1).getMALOAISP();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public class ViewHolderMenu{
        TextView txtTenLoaiSP;
        ImageView imgMenu;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View viewGroupCha = view;
        if(viewGroupCha == null){
            viewHolderMenu = new ViewHolderMenu();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewGroupCha = inflater.inflate(R.layout.custom_layout_group_cha, viewGroup, false);

            viewHolderMenu.txtTenLoaiSP = viewGroupCha.findViewById(R.id.txtTenLoaiSP);
            viewHolderMenu.imgMenu = viewGroupCha.findViewById(R.id.imgMenu);
            viewGroupCha.setTag(viewHolderMenu);
        }else{
            viewHolderMenu = (ViewHolderMenu) viewGroupCha.getTag();
        }


        viewHolderMenu.txtTenLoaiSP.setText(loaiSanPhamList.get(i).getTENLOAISP());
        int dem = loaiSanPhamList.get(i).getListCon().size();
        if(dem>0){
            viewHolderMenu.imgMenu.setVisibility(View.VISIBLE);
        }else {
            viewHolderMenu.imgMenu.setVisibility(View.INVISIBLE);
        }
        if(b){
            viewHolderMenu.imgMenu.setImageResource(R.drawable.ic_show_group2);
            viewGroupCha.setBackgroundResource(R.color.gray);

        }else{
            viewHolderMenu.imgMenu.setImageResource(R.drawable.ic_show_group);
        }
        viewGroupCha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("zzzz", loaiSanPhamList.get(i).getTENLOAISP() + loaiSanPhamList.get(i).getMALOAISP());
                return false;
            }
        });
        return viewGroupCha;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        SecondExpandable secondExpandable = new SecondExpandable(context);
        ExpandAdapter secondAdapter = new ExpandAdapter(context, loaiSanPhamList.get(i).getListCon());
        secondExpandable.setAdapter(secondAdapter);
        secondExpandable.setGroupIndicator(null);
        notifyDataSetChanged();
        return secondExpandable;
    }

    public class SecondExpandable extends ExpandableListView{
        public SecondExpandable(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            Log.d("ZZZZ", width +"x"+height);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
