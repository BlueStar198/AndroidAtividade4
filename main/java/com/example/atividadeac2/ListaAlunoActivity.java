package com.example.atividadeac2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.atividadeac2.adaptador.AlunoAdapter;
import com.example.atividadeac2.models.Aluno;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaAlunoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlunoAdapter alunoAdapter;
    private List<Aluno> listaAlunos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_aluno);

        recyclerView = findViewById(R.id.recyclerViewAlunos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alunoAdapter = new AlunoAdapter(listaAlunos);
        recyclerView.setAdapter(alunoAdapter);

        buscarAlunos();
    }

    private void buscarAlunos() {
        String url = "https://67340030a042ab85d1189c58.mockapi.io/alunos"; // URL da API

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray alunosJsonArray = response;
                        for (int i = 0; i < alunosJsonArray.length(); i++) {
                            JSONObject alunoJson = alunosJsonArray.getJSONObject(i);
                            Aluno aluno = new Aluno(
                                    alunoJson.getInt("ra"),
                                    alunoJson.getString("nome"),
                                    alunoJson.getString("cep"),
                                    alunoJson.getString("logradouro"),
                                    alunoJson.getString("complemento"),
                                    alunoJson.getString("bairro"),
                                    alunoJson.getString("cidade"),
                                    alunoJson.getString("uf")
                            );
                            listaAlunos.add(aluno);
                        }
                        alunoAdapter.notifyDataSetChanged(); // Atualiza o RecyclerView
                    } catch (JSONException e) {
                        Toast.makeText(ListaAlunoActivity.this, "Erro ao carregar alunos.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(ListaAlunoActivity.this, "Falha ao buscar alunos.", Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }
}
