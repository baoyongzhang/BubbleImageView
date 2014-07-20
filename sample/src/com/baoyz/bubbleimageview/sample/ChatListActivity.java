package com.baoyz.bubbleimageview.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChatListActivity extends Activity {

	private ListView mListView;
	private ListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_list);

		List<ChatModel> list = new ArrayList<ChatModel>();
		for (int i = 0; i < 100; i++) {
			ChatModel item = new ChatModel();
			item.type = (int) (Math.random() * 4);
			item.content = "text content " + i;
			item.imgId = R.drawable.pic1 + (int)(Math.random() * 4);
			list.add(item);
		}

		mListView = (ListView) findViewById(R.id.lv_chat);
		mAdapter = new ChatAdapter(list);
		mListView.setAdapter(mAdapter);
	}

	static class ChatAdapter extends BaseAdapter {

		List<ChatModel> mChatList;

		public ChatAdapter(List<ChatModel> list) {
			mChatList = list;
		}

		@Override
		public int getCount() {
			return mChatList.size();
		}

		@Override
		public ChatModel getItem(int position) {
			return mChatList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			return 4;
		}

		@Override
		public int getItemViewType(int position) {
			return getItem(position).type;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChatModel item = getItem(position);
			switch (item.type) {
			case ChatModel.TYPE_LEFT_TEXT:
				if (convertView == null) {
					convertView = View.inflate(parent.getContext(),
							R.layout.item_chat_left_text, null);
				}
				((TextView) convertView.findViewById(R.id.tv_text))
						.setText(item.content);
				break;
			case ChatModel.TYPE_LEFT_IMG:
				if (convertView == null) {
					convertView = View.inflate(parent.getContext(),
							R.layout.item_chat_left_img, null);
				}
				((ImageView) convertView.findViewById(R.id.iv_img))
						.setImageResource(item.imgId);
				break;
			case ChatModel.TYPE_RIGHT_TEXT:
				if (convertView == null) {
					convertView = View.inflate(parent.getContext(),
							R.layout.item_chat_right_text, null);
				}
				((TextView) convertView.findViewById(R.id.tv_text))
						.setText(item.content);
				break;
			case ChatModel.TYPE_RIGHT_IMG:
				if (convertView == null) {
					convertView = View.inflate(parent.getContext(),
							R.layout.item_chat_right_img, null);
				}
				((ImageView) convertView.findViewById(R.id.iv_img))
						.setImageResource(item.imgId);
				break;
			}
			return convertView;
		}

	}

	static class ChatModel {
		public static final int TYPE_LEFT_TEXT = 0;
		public static final int TYPE_LEFT_IMG = 1;
		public static final int TYPE_RIGHT_TEXT = 2;
		public static final int TYPE_RIGHT_IMG = 3;
		public int type;
		public String content;
		public int imgId;
	}
}
