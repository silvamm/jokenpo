package br.com.matheusmonteiro.jokenpo;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public  class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public ImageButton imgBtnPedra;
    public ImageButton imgBtnPapel;
    public ImageButton imgBtnTesoura;
    public ImageView imgViewJogador;
    public ImageView imgViewMaquina;
    public AnimationDrawable animation;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgBtnPedra = findViewById(R.id.imgBtnPedra);
        imgBtnPapel = findViewById(R.id.imgBtnPapel);
        imgBtnTesoura = findViewById(R.id.imgBtnTesoura);
        imgViewJogador = findViewById(R.id.imgViewEscolhaJogador);
        imgViewMaquina = findViewById(R.id.imgViewEscolhaMaquina);
        pontosVitorias = findViewById(R.id.pontosVitoria);
        pontosDerrotas = findViewById(R.id.pontosDerrotas);
        pontosEmpates = findViewById(R.id.pontosEmpates);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


    public void animacao(){

        imgViewMaquina.setImageResource(R.drawable.animacao);
        animation = (AnimationDrawable) imgViewMaquina.getDrawable();
        animation.start();

        Animation deslocamento = new TranslateAnimation(0, 0, 0, 0);
        deslocamento.setDuration(3000);
        imgViewMaquina.startAnimation(deslocamento);

    }

    public void tocarMusicaJogada(JogadaEnum jogadaUsuario){

        if(tocador != null){
            tocador.stop();
        }
        tocador = MediaPlayer.create(this, R.raw.alex_play);
        tocador.start();
        animacao();
        tocador.setOnCompletionListener(mp -> {

            completarJogada(jogadaUsuario);
            animation.stop();

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
