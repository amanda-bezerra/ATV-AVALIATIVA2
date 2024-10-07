package br.com.amanda.atv_avaliativa2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ComprovanteActivity extends AppCompatActivity {

    TextView comprovanteDetalhes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprovante);

        comprovanteDetalhes = findViewById(R.id.comprovanteDetalhes);

        // Recebe os dados enviados pela MainActivity
        String filme = getIntent().getStringExtra("FILME");
        String horario = getIntent().getStringExtra("HORARIO");
        String cadeiras = getIntent().getStringExtra("CADEIRAS");
        int quantidade = getIntent().getIntExtra("QUANTIDADE", 0);
        double valorTotal = getIntent().getDoubleExtra("VALOR_TOTAL", 0.0);

        // Monta o texto para o comprovante
        String comprovanteTexto = "Comprovante de Compra\n\n" +
                "Filme: " + filme + "\n" +
                "Horário: " + horario + "\n" +
                "Cadeiras: " + cadeiras + "\n" +
                "Quantidade: " + quantidade + "\n" +
                "Valor Total: R$ " + String.format("%.2f", valorTotal);

        // Exibe os detalhes da compra no TextView
        comprovanteDetalhes.setText(comprovanteTexto);
    }
}
