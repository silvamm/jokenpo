package br.com.matheusmonteiro.jokenpo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import br.com.matheusmonteiro.jokenpo.bean.Usuario;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ProgressBar progressBar;
    Button btEntrar;
    SignInButton btnGoogle;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    Usuario usuario = new Usuario();
    GoogleSignInClient mGoogleSignInClient;
    NavigationView navigationView;
    private final static int GOOGLE_SIGN = 123;

    @Override
    public void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(LoginActivity.this);

        progressBar = findViewById(R.id.progressBar);
        btEntrar = findViewById(R.id.btEntrar);
        btnGoogle = findViewById(R.id.btnGoogle);
        setGoogleButtonText(btnGoogle, "ENTRE COM O GOOGLE");

        mAuth = FirebaseAuth.getInstance();

        btnGoogle.setOnClickListener(btn -> signIn());

        btEntrar.setOnClickListener(b -> {
            EditText inputEmail = findViewById(R.id.inputUsuario);

            EditText inputSenha = findViewById(R.id.inputSenha);
            efetuarLogin(inputEmail, inputSenha);
        });

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

         navigationView = findViewById(R.id.nav_view);


    }

    private void signIn(){
        progressDialog.setTitle("Carregando");
        progressDialog.setMessage("Um pouco de paciência");
        progressDialog.show();
        //progressBar.setVisibility(View.VISIBLE);
        Intent signIntent  = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> result =  GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = result.getResult(ApiException.class);
                if(account != null){
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                //progressBar.setVisibility(View.INVISIBLE);
                Log.i("naopegou", "signInWithCredential:  " + e.getMessage());
                Toast.makeText(LoginActivity.this, "Autenticação com Google falhou", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signOut();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("pegou", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w("naopegou", "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Autenticacao com Firebase falhou", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    //progressBar.setVisibility(View.INVISIBLE);
                });

    }

    @Override
    public void onBackPressed() {
        // não chame o super desse método
    }

    private void updateUI(FirebaseUser user) {

        Intent intent = new Intent();


        if(user != null){
            intent.setClass(LoginActivity.this, MainActivity.class);
            usuario.setFirebase(user.getUid());
            if(user.getPhotoUrl() != null){
                usuario.setFoto(user.getPhotoUrl().toString());
            }
            if(user.getDisplayName() != null){
                usuario.setNome(user.getDisplayName());
            }else{
                usuario.setNome("Jogador");
            }
            usuario.setEmail(user.getEmail());
            intent.putExtra(Usuario.class.getSimpleName(), usuario);
        }

        startActivity(intent);
    }

    protected void setGoogleButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);

                return;
            }
        }
    }

    public void efetuarLogin(EditText inputEmail, EditText inputSenha){

        inputEmail.setError(null);
        inputSenha.setError(null);

        String email = inputEmail.getText().toString();
        String senha = inputSenha.getText().toString();

        boolean temErro = false;
        View campoComErro = null;

        if (TextUtils.isEmpty(senha)) {
            inputSenha.setError(getString(R.string.campoObrigatorio));
            campoComErro = inputSenha;
            temErro = true;
        }

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.campoObrigatorio));
            campoComErro = inputEmail;
            temErro = true;
        } else if (!emailValido(email)) {
            inputEmail.setError(getString(R.string.emailInvalido));
            campoComErro = inputEmail;
            temErro = true;
        }

        if (temErro) {
            campoComErro.requestFocus();
        } else {
            verificarCredenciais(email, senha);

        }

    }

    private void verificarCredenciais(String email, String senha){

        if(email.equals("admin@stos.mobi") && senha.equals("123456")){
            alert("Logado com sucesso");
        }else{
            alert("E-mail ou senha incorretos");
        }
    }

    private boolean emailValido(String email) {
        boolean temPontoDepoisDoArroba = false;
        boolean temArroba = email.contains("@");
        if(temArroba){
            String depoisDoArroba = email.substring(email.lastIndexOf("@"));
            temPontoDepoisDoArroba = depoisDoArroba.contains(".");
        }
        return temArroba && temPontoDepoisDoArroba;
    }

    private void alert(String mensagem){
        Toast toast = Toast.makeText(this, mensagem, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home_btn_exit:
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut().addOnCompleteListener(this,
                        task ->  updateUI(null));
                              return true;

            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }
}
