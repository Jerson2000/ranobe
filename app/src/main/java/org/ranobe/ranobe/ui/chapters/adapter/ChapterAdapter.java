package org.ranobe.ranobe.ui.chapters.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ranobe.ranobe.databinding.ItemChapterBinding;
import org.ranobe.ranobe.models.Chapter;
import org.ranobe.ranobe.models.ReadHistory;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.MyViewHolder> {
    private final List<Chapter> items;
    private final OnChapterItemClickListener listener;
    private List<ReadHistory> historyList;

    public ChapterAdapter(List<Chapter> items, OnChapterItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public ChapterAdapter(List<Chapter> items,List<ReadHistory> historyList, OnChapterItemClickListener listener) {
        this.historyList = historyList;
        this.listener = listener;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChapterBinding binding = ItemChapterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chapter item = items.get(position);
        boolean isRead = false;
        if (historyList != null) {
            for (ReadHistory history : historyList) {
                if (history != null && history.url.equals(item.url)) {
                    isRead = true;
                    break;
                }
            }
        }
        holder.binding.chapterItemLayout.setAlpha(isRead ? 0.5F : 1.0F);
        holder.binding.chapterName.setText(item.name);
        if (item.updated != null && !item.updated.isEmpty())
            holder.binding.updated.setText(item.updated);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnChapterItemClickListener {
        void onChapterItemClick(Chapter item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemChapterBinding binding;

        public MyViewHolder(@NonNull ItemChapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.chapterItemLayout.setOnClickListener(v ->
                    listener.onChapterItemClick(items.get(getAdapterPosition())));
        }
    }
}
