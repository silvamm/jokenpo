package br.com.matheusmonteiro.jokenpo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public ImageButton imgBtnPedra;
    public ImageButton imgBtnPapel;
    public ImageButton imgBtnTesoura;
    public ImageView imgViewJogador;
    public ImageView imgViewMaquina;
    public Random jogadaMaquina;
    public MediaPlayer tocador;
    public TextView pontosVitorias;
    public TextView pontosDerrotas;
    public TextView pontosEmpates;
    public int empates = 0;
    public int vitorias = 0;
    public int derrotas = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgBtnPedra = findViewById(R.id.imgBtnPedra);
        imgBtnPapel = findViewById(R.id.imgBtnPapel);
        imgBtnTesoura = findViewById(R.id.imgBtnTesoura);
        imgViewJogador = findViewById(R.id.imgViewEscolhaJogador);
        imgViewMaquina = findViewById(R.id.imgViewEscolhaMaquina);
        pontosVitorias = findViewById(R.id.pontosVitoria);
        pontosDerrotas = findViewById(R.id.pontosDerrotas);
        pontosEmpates = findViewById(R.id.pontosEmpates);

        jogadaMaquina = new Random();

        imgBtnPedra.setOnClickListener(b -> {
            imgViewJogador.setImageResource(R.drawable.pedra_jogador);
            iniciarJogada(JogadaEnum.PEDRA);
            desativarBotoes();
        });

        imgBtnTesoura.setOnClickListener(b -> {
            imgViewJogador.setImageResource(R.drawable.tesoura_jogador);
            iniciarJogada(JogadaEnum.TESOURA);
            desativarBotoes();
        });

        imgBtnPapel.setOnClickListener(b -> {
            imgViewJogador.setImageResource(R.drawable.papel_jogador);
            iniciarJogada(JogadaEnum.PAPEL);
            desativarBotoes();
        });

    }

    public void desativarBotoes(){
        imgBtnPapel.setEnabled(false);
        imgBtnTesoura.setEnabled(false);
        imgBtnPedra.setEnabled(false);
    }

    public void ativarBotoes(){
        imgBtnPapel.setEnabled(true);
        imgBtnTesoura.setEnabled(true);
        imgBtnPedra.setEnabled(true);

    }

    public void iniciarJogada(JogadaEnum jogadaUsuario){

        tocarMusicaJogada(jogadaUsuario);
    }

    public void completarJogada(JogadaEnum jogadaUsuario){

        JogadaEnum jogadaPC = JogadaEnum.getJogadaRandom();

        switch (jogadaPC){
            case PEDRA:
                imgViewMaquina.setImageResource(R.drawable.pedra);
                break;
            case PAPEL:
                imgViewMaquina.setImageResource(R.drawable.papel);
                break;
            case TESOURA:
                imgViewMaquina.setImageResource(R.drawable.tesoura);
                break;
        }

//        Thread t1 = new Thread(() -> {
//            try {
//                Thread.sleep(Toast.LENGTH_SHORT);
//                finalizar();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//        });
//        t1.start();

        if(jogadaPC.equals(jogadaUsuario)){
            empate();
        }else if(jogadaPC.equals(JogadaEnum.PAPEL) && jogadaUsuario.equals(JogadaEnum.PEDRA)) {
            derrota();
        }else if(jogadaPC.equals(JogadaEnum.PAPEL) && jogadaUsuario.equals(JogadaEnum.TESOURA)){
            ganhou();
        }else if(jogadaPC.equals(JogadaEnum.PEDRA) && jogadaUsuario.equals(JogadaEnum.PAPEL)){
            ganhou();
        }else if(jogadaPC.equals(JogadaEnum.PEDRA) && jogadaUsuario.equals(JogadaEnum.TESOURA)) {
            derrota();
        }else if(jogadaPC.equals(JogadaEnum.TESOURA) && jogadaUsuario.equals(JogadaEnum.PEDRA)) {
            ganhou();
        }else if(jogadaPC.equals(JogadaEnum.TESOURA) && jogadaUsuario.equals(JogadaEnum.PAPEL)){
            derrota();
        }

    }

//    public void finalizar(){
//        imgViewMaquina.setImageResource(R.drawable.interrogacao);
//    }


    public void empate(){
        Toast.makeText(MainActivity.this,"EMPATE!",Toast.LENGTH_SHORT).show();
        empates++;
        pontosEmpates.setText(String.valueOf(empates));
        tocarMusicaEmpate();
        ativarBotoes();


    }

    public void derrota(){
        Toast.makeText(MainActivity.this,"PERDEU!",Toast.LENGTH_SHORT).show();
        derrotas++;
        pontosDerrotas.setText(String.valueOf(derrotas));
        tocarMusicaDerrota();
        ativarBotoes();


    }

    public void ganhou(){
        Toast.makeText(MainActivity.this,"GANHOU!",Toast.LENGTH_SHORT).show();
        vitorias++;
        pontosVitorias.setText(String.valueOf(vitorias));
        tocarMusicaVitoria();

    }



    public void tocarMusicaJogada(JogadaEnum jogadaUsuario){

        if(tocador != null){
            tocador.stop();
        }
        tocador = MediaPlayer.create(this, R.raw.alex_play);
        tocador.start();
        tocador.setOnCompletionListener(mp -> {
            completarJogada(jogadaUsuario);

        });

    }

    public void tocarMusicaEmpate(){

    }

    public void tocarMusicaVitoria(){

        if(tocador != null){
            tocador.stop();
        }
        tocador = MediaPlayer.create(this, R.raw.aplausos);
        tocador.start();
        tocador.seekTo(4000);
        tocador.setOnCompletionListener(mp -> {
            ativarBotoes();

        });

    }

    public void tocarMusicaDerrota(){

    }
    public enum JogadaEnum {

        PEDRA(0), PAPEL(1), TESOURA(2);

        private final int valor;
        JogadaEnum(int valor) {
            this.valor = valor;
        }

        public int getValor(){
            return this.valor;
        }

        private static final List<JogadaEnum> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static JogadaEnum getJogadaRandom()  {

            return VALUES.get(RANDOM.nextInt(SIZE));
        }

    };
}
