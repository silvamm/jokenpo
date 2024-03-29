package br.com.matheusmonteiro.jokenpo;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import br.com.matheusmonteiro.jokenpo.bean.Usuario;

public  class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public View cabecalho;
    public ImageButton imgBtnPedra;
    public ImageButton imgBtnPapel;
    public ImageButton imgBtnTesoura;
    public ImageView imgViewJogador;
    public ImageView imgViewMaquina;
    public ImageView imgFotoJogador;
    public AnimationDrawable animation;
    public Random jogadaMaquina;
    public MediaPlayer tocador;
    public TextView pontosVitorias;
    public TextView pontosDerrotas;
    public TextView pontosEmpates;
    public TextView menuLateralNomeJogador;
    public TextView menuLateralEmailJogador;
    public Usuario jogador;
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
        menuLateralNomeJogador = findViewById(R.id.nav_header_title);
        menuLateralEmailJogador = findViewById(R.id.nav_header_title_email);
        imgFotoJogador = findViewById(R.id.nav_header_foto);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        jogador = (Usuario) getIntent().getSerializableExtra(Usuario.class.getSimpleName());
        if(jogador != null){

            cabecalho = navigationView.getHeaderView(0);

            menuLateralNomeJogador = cabecalho.findViewById(R.id.nav_header_title);
            menuLateralEmailJogador = cabecalho.findViewById(R.id.nav_header_title_email);
            imgFotoJogador = cabecalho.findViewById(R.id.nav_header_foto);

            Picasso.get().load(jogador.getFoto()).into(imgFotoJogador);

            menuLateralEmailJogador.setText(jogador.getEmail());
            menuLateralNomeJogador.setText(jogador.getNome());


         }

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_home_btn_exit:
                logOut();
                return true;

            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }

    }

    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivity(intent);
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


    public void comecarAnimacao(){

        imgViewMaquina.setImageResource(R.drawable.animacao);
        animation = (AnimationDrawable) imgViewMaquina.getDrawable();
        animation.start();

        Animation deslocamento = new TranslateAnimation(0, 0, 0, 0);
        deslocamento.setDuration(3000);
        imgViewMaquina.startAnimation(deslocamento);

    }

    public void pararAnimacao(){
        if(animation != null){
            animation.stop();
        }
    }

    public void tocarMusicaJogada(JogadaEnum jogadaUsuario){

        if(tocador != null){
            tocador.stop();
        }
        tocador = MediaPlayer.create(this, R.raw.alex_play);
        tocador.start();
        comecarAnimacao();
        tocador.setOnCompletionListener(mp -> {

            completarJogada(jogadaUsuario);
            pararAnimacao();


        });

    }

    public void tocarMusicaEmpate(){

    }

    public void tocarMusicaVitoria(){

        if(tocador != null){
            tocador.stop();
        }
//        tocador = MediaPlayer.create(this, R.raw.aplausos);
//        tocador.start();
//        tocador.seekTo(4000);
//        tocador.setOnCompletionListener(mp -> {
//            ativarBotoes();
//
//        });

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
        Toast.makeText(MainActivity.this,"NÃO FOI DESSA VEZ! TENTE NOVAMENTE!",Toast.LENGTH_SHORT).show();
        derrotas++;
        pontosDerrotas.setText(String.valueOf(derrotas));
        tocarMusicaDerrota();
        ativarBotoes();


    }

    public void ganhou(){
        Toast.makeText(MainActivity.this,"GANHOU! PARABÉNS!",Toast.LENGTH_SHORT).show();
        vitorias++;
        pontosVitorias.setText(String.valueOf(vitorias));
        tocarMusicaVitoria();
        ativarBotoes();
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
