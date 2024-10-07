package br.com.amanda.atv_avaliativa2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    // Elementos da interface
    Spinner spinnerFilmes, spinnerHorarios;
    EditText editQuantidade;
    GridLayout gridCadeiras;
    TextView selectedSeat;
    Button btnComprar;

    // Banco de dados
    ManagerDB managerDB;

    // Variáveis para controlar a seleção
    String filmeSelecionado = "";
    String horarioSelecionado = "";
    HashSet<ImageView> cadeirasSelecionadas = new HashSet<>();  // Conjunto de cadeiras selecionadas
    boolean[][] cadeiraDisponivel = new boolean[2][10];  // Agora, 2 filas e 10 colunas
    final double precoPorIngresso = 20.00;  // Preço fixo de cada ingresso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializando os elementos da interface
        spinnerFilmes = findViewById(R.id.spinnerFilmes);
        spinnerHorarios = findViewById(R.id.spinnerHorarios);
        editQuantidade = findViewById(R.id.editQuantidade);
        gridCadeiras = findViewById(R.id.gridCadeiras);
        selectedSeat = findViewById(R.id.selectedSeat);
        btnComprar = findViewById(R.id.btnComprar);

        // Inicializando o banco de dados
        managerDB = new ManagerDB(this);

        // Configurando o Spinner de filmes
        String[] filmes = {"Vingadores: Ultimato", "O Rei Leão", "Coringa", "Star Wars: O Despertar da Força"};
        ArrayAdapter<String> adapterFilmes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filmes);
        adapterFilmes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilmes.setAdapter(adapterFilmes);

        // Pega o filme selecionado
        spinnerFilmes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filmeSelecionado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filmeSelecionado = "";
            }
        });

        // Configurando o Spinner de horários
        String[] horarios = {"14:00", "16:00", "18:00", "20:00"};
        ArrayAdapter<String> adapterHorarios = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, horarios);
        adapterHorarios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarios.setAdapter(adapterHorarios);

        // Pega o horário selecionado
        spinnerHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horarioSelecionado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                horarioSelecionado = "";
            }
        });

        // Inicializando o estado das cadeiras (disponível ou ocupada)
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                cadeiraDisponivel[i][j] = true;  // Todas as cadeiras começam como disponíveis
            }
        }

        // Exemplo de cadeiras ocupadas
        cadeiraDisponivel[0][5] = false;
        cadeiraDisponivel[1][7] = false;

        // Preenche o GridLayout com cadeiras (ImageViews)
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                final ImageView cadeira = new ImageView(this);

                // Define a imagem da cadeira (disponível ou ocupada)
                if (cadeiraDisponivel[i][j]) {
                    cadeira.setImageResource(R.drawable.cadeira);  // Cadeira disponível
                } else {
                    cadeira.setImageResource(R.drawable.cadeira_selecionada);  // Cadeira ocupada
                    cadeira.setEnabled(false);  // Desabilita o clique em cadeiras ocupadas
                }

                // Define o tamanho da cadeira
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 80;  // Largura da cadeira
                params.height = 80; // Altura da cadeira
                params.setMargins(5, 5, 5, 5);  // Margens entre cadeiras
                cadeira.setLayoutParams(params);

                // Define a ação de clique na cadeira
                final int row = i;
                final int col = j;
                cadeira.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cadeiraDisponivel[row][col]) {
                            if (cadeirasSelecionadas.contains(cadeira)) {
                                // Se a cadeira já estava selecionada, desmarcá-la (volta para a imagem padrão)
                                cadeira.setImageResource(R.drawable.cadeira);
                                cadeirasSelecionadas.remove(cadeira);
                            } else {
                                // Se a cadeira não estava selecionada, marcá-la (muda para a imagem de selecionada)
                                cadeira.setImageResource(R.drawable.cadeira_selecionada);
                                cadeirasSelecionadas.add(cadeira);
                                // Atualizar o texto com o número da cadeira
                                selectedSeat.setText("Poltrona " + ((row * 10) + col + 1) + " selecionada");
                            }

                            // Atualiza o campo de quantidade com o número de cadeiras selecionadas
                            editQuantidade.setText(String.valueOf(cadeirasSelecionadas.size()));

                            // Se não houver cadeiras selecionadas, exibe o texto padrão
                            if (cadeirasSelecionadas.isEmpty()) {
                                selectedSeat.setText("Nenhuma cadeira selecionada");
                            } else {
                                selectedSeat.setText("Total de Poltronas Selecionadas: " + cadeirasSelecionadas.size());
                            }
                        }
                    }
                });

                // Adiciona a cadeira ao grid
                gridCadeiras.addView(cadeira);
            }
        }

        // Ação ao clicar em "Confirmar Compra"
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filmeSelecionado.isEmpty() || horarioSelecionado.isEmpty() || cadeirasSelecionadas.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                } else {
                    int quantidade = cadeirasSelecionadas.size();  // Número de cadeiras selecionadas
                    double valorTotal = quantidade * precoPorIngresso;  // Calcula o valor total

                    // Insere os dados no banco de dados
                    boolean isInserted = managerDB.insertIngresso(filmeSelecionado, horarioSelecionado, quantidade);
                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "Compra realizada com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Erro ao realizar a compra.", Toast.LENGTH_SHORT).show();
                    }

                    // Envia os detalhes da compra para a nova Activity (ComprovanteActivity)
                    Intent intent = new Intent(MainActivity.this, ComprovanteActivity.class);
                    intent.putExtra("FILME", filmeSelecionado);
                    intent.putExtra("HORARIO", horarioSelecionado);
                    intent.putExtra("CADEIRAS", cadeirasSelecionadas.toString());
                    intent.putExtra("QUANTIDADE", quantidade);
                    intent.putExtra("VALOR_TOTAL", valorTotal);
                    startActivity(intent);  // Abre a nova tela de comprovante
                }
            }
        });
    }
}
