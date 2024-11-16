package com.example.atividadeac2.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.atividadeac2.R;
import com.example.atividadeac2.models.Aluno;

import java.util.List;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder> {

    private List<Aluno> alunos;

    public AlunoAdapter(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    @Override
    public AlunoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aluno, parent, false);
        return new AlunoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlunoViewHolder holder, int position) {
        Aluno aluno = alunos.get(position);
        holder.raTextView.setText(String.valueOf(aluno.getRa()));
        holder.nomeTextView.setText(aluno.getNome());
        holder.cepTextView.setText(aluno.getCep());
        holder.logradouroTextView.setText(aluno.getLogradouro());
        holder.bairroTextView.setText(aluno.getBairro());
        holder.cidadeTextView.setText(aluno.getCidade());
        holder.ufTextView.setText(aluno.getUf());
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public static class AlunoViewHolder extends RecyclerView.ViewHolder {
        TextView raTextView;
        TextView nomeTextView;
        TextView cepTextView;
        TextView logradouroTextView;
        TextView bairroTextView;
        TextView cidadeTextView;
        TextView ufTextView;

        public AlunoViewHolder(View itemView) {
            super(itemView);
            raTextView = itemView.findViewById(R.id.raTextView);
            nomeTextView = itemView.findViewById(R.id.nomeTextView);
            cepTextView = itemView.findViewById(R.id.cepTextView);
            logradouroTextView = itemView.findViewById(R.id.logradouroTextView);
            bairroTextView = itemView.findViewById(R.id.bairroTextView);
            cidadeTextView = itemView.findViewById(R.id.cidadeTextView);
            ufTextView = itemView.findViewById(R.id.ufTextView);
        }
    }
}
