package com.example.atividadeac2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.atividadeac2.models.Aluno;

import org.json.JSONException;
import org.json.JSONObject;

public class CadastroActivity extends AppCompatActivity {

    private EditText editRa, editNome, editCep, editLogradouro, editComplemento, editBairro, editCidade, editUf;
    private Button btnCadastrar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editRa = findViewById(R.id.editRa);
        editNome = findViewById(R.id.editNome);
        editCep = findViewById(R.id.editCep);
        editLogradouro = findViewById(R.id.editLogradouro);
        editComplemento = findViewById(R.id.editComplemento);
        editBairro = findViewById(R.id.editBairro);
        editCidade = findViewById(R.id.editCidade);
        editUf = findViewById(R.id.editUf);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        progressBar = findViewById(R.id.progressBar);

        // Monitora mudanças no CEP
        editCep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String cep = editable.toString();
                if (cep.length() == 8) { // Verifica se o CEP tem 8 caracteres
                    buscarEnderecoPorCep(cep);
                }
            }
        });

        // Ação do botão para cadastrar o aluno
        btnCadastrar.setOnClickListener(v -> cadastrarAluno());
    }

    private void buscarEnderecoPorCep(String cep) {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Preenche os campos de endereço se a resposta tiver os dados
                        if (response.has("logradouro")) {
                            editLogradouro.setText(response.getString("logradouro"));
                            editBairro.setText(response.getString("bairro"));
                            editCidade.setText(response.getString("localidade"));
                            editUf.setText(response.getString("uf"));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(CadastroActivity.this, "Erro ao carregar endereço.", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    Toast.makeText(CadastroActivity.this, "Falha ao buscar endereço.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void cadastrarAluno() {
        String ra = editRa.getText().toString();
        String nome = editNome.getText().toString();
        String cep = editCep.getText().toString();
        String logradouro = editLogradouro.getText().toString();
        String complemento = editComplemento.getText().toString();
        String bairro = editBairro.getText().toString();
        String cidade = editCidade.getText().toString();
        String uf = editUf.getText().toString();

        // Verifica se todos os campos foram preenchidos
        if (ra.isEmpty() || nome.isEmpty() || cep.isEmpty() || logradouro.isEmpty() || bairro.isEmpty() || cidade.isEmpty() || uf.isEmpty()) {
            Toast.makeText(CadastroActivity.this, "Todos os campos devem ser preenchidos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria um objeto Aluno
        Aluno aluno = new Aluno(Integer.parseInt(ra), nome, cep, logradouro, complemento, bairro, cidade, uf);

        String url = "https://67340030a042ab85d1189c58.mockapi.io/alunos"; // URL do MockAPI
        JSONObject alunosJson = new JSONObject();
        try {
            alunosJson.put("ra", aluno.getRa());
            alunosJson.put("nome", aluno.getNome());
            alunosJson.put("cep", aluno.getCep());
            alunosJson.put("logradouro", aluno.getLogradouro());
            alunosJson.put("complemento", aluno.getComplemento());
            alunosJson.put("bairro", aluno.getBairro());
            alunosJson.put("cidade", aluno.getCidade());
            alunosJson.put("uf", aluno.getUf());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressBar.setVisibility(View.VISIBLE); // Exibe a ProgressBar durante o processo
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, alunosJson,
                response -> {
                    Toast.makeText(CadastroActivity.this, "Aluno cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish(); // Finaliza a Activity após o cadastro
                },
                error -> {
                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar aluno.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
