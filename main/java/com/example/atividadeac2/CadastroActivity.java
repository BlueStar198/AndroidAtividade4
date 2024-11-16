package com.example.atividadeac2;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

public class CadastroActivity extends AppCompatActivity {

    private EditText editRa, editNome, editCep, editLogradouro, editComplemento, editBairro, editCidade, editUf;
    private Button btnCadastrar;
    private Button btnIrParaLista;
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
        btnIrParaLista = findViewById(R.id.btnIrParaListagem);
        progressBar = findViewById(R.id.progressBar);


        editCep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String cep = editable.toString();
                if (cep.length() == 8 && cep.matches("\\d+")) {
                    buscarEnderecoPorCep(cep);
                } else if (cep.length() == 8) {
                    Toast.makeText(CadastroActivity.this, "CEP inválido. Insira apenas números.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCadastrar.setOnClickListener(v -> cadastrarAluno());
        btnIrParaLista.setOnClickListener(v -> verLista());
    }

    private void buscarEnderecoPorCep(String cep) {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.has("erro") && response.getBoolean("erro")) {
                            limparCamposEndereco();
                            Toast.makeText(CadastroActivity.this, "CEP não encontrado.", Toast.LENGTH_SHORT).show();
                        } else {
                            editLogradouro.setText(response.optString("logradouro", ""));
                            editBairro.setText(response.optString("bairro", ""));
                            editCidade.setText(response.optString("localidade", ""));
                            editUf.setText(response.optString("uf", ""));
                        }
                    } catch (JSONException e) {
                        limparCamposEndereco();
                        Toast.makeText(CadastroActivity.this, "Erro ao carregar endereço.", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    limparCamposEndereco();
                    Toast.makeText(CadastroActivity.this, "Falha ao buscar endereço.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void limparCamposEndereco() {
        editLogradouro.setText("");
        editBairro.setText("");
        editCidade.setText("");
        editUf.setText("");
    }

    private void cadastrarAluno() {
        String ra = editRa.getText().toString().trim();
        String nome = editNome.getText().toString().trim();
        String cep = editCep.getText().toString().trim();
        String logradouro = editLogradouro.getText().toString().trim();
        String complemento = editComplemento.getText().toString().trim();
        String bairro = editBairro.getText().toString().trim();
        String cidade = editCidade.getText().toString().trim();
        String uf = editUf.getText().toString().trim();

        if (ra.isEmpty() || nome.isEmpty() || cep.isEmpty() || logradouro.isEmpty() || bairro.isEmpty() || cidade.isEmpty() || uf.isEmpty()) {
            Toast.makeText(CadastroActivity.this, "Todos os campos devem ser preenchidos.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject alunosJson = new JSONObject();
        try {
            alunosJson.put("ra", ra);
            alunosJson.put("nome", nome);
            alunosJson.put("cep", cep);
            alunosJson.put("logradouro", logradouro);
            alunosJson.put("complemento", complemento);
            alunosJson.put("bairro", bairro);
            alunosJson.put("cidade", cidade);
            alunosJson.put("uf", uf);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressBar.setVisibility(View.VISIBLE);
        String url = "https://67340030a042ab85d1189c58.mockapi.io/alunos";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, alunosJson,
                response -> {
                    Toast.makeText(CadastroActivity.this, "Aluno cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(CadastroActivity.this, ListaAlunoActivity.class);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar aluno.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void verLista() {
        Intent intent = new Intent(CadastroActivity.this, ListaAlunoActivity.class);
        startActivity(intent);
        finish();
    }
}
