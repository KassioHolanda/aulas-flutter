package com.android.educar.educar.network.service;

import com.android.educar.educar.network.helpers.AlunoFrequenciaMesEndPoint;
import com.android.educar.educar.network.helpers.AlunoNotaMesEndPoint;
import com.android.educar.educar.network.helpers.AnoLetivoEndPoint;
import com.android.educar.educar.network.helpers.BimestreEndPoint;
import com.android.educar.educar.network.helpers.CorpoEndPoint;
import com.android.educar.educar.network.helpers.FuncionarioEndPoint;
import com.android.educar.educar.network.helpers.MatriculaEndPoint;
import com.android.educar.educar.network.helpers.OcorrenciaEndPoint;
import com.android.educar.educar.network.helpers.PessoaFisicaEndPoint;
import com.android.educar.educar.network.helpers.SituacaoTurmaMesEndPoint;
import com.android.educar.educar.network.helpers.TipoOcorrenciaEndPoint;
import com.android.educar.educar.network.helpers.TokenEndPoint;
import com.android.educar.educar.network.helpers.TurmaEndPoint;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIService {

    public static String TAG = APIService.class.getSimpleName();


//    public static final String BASE_URL = "http://10.20.30.119:8000/";
    //    public static final String BASE_URL = "http:/172.16.0.149:8000/";
//    public static final String BASE_URL = "http:/192.168.0.103:8000/";
    public static final String BASE_URL = "http://apieducar.deltatecnologia.net.br/";
    private Retrofit retrofit;

    private Interceptor interceptor;
    private TurmaEndPoint turmaEndPoint;
    private FuncionarioEndPoint funcionarioEndPoint;
    private PessoaFisicaEndPoint pessoaFisicaEndPoint;
    private MatriculaEndPoint matriculaEndPoint;
    private AlunoFrequenciaMesEndPoint alunoFrequenciaMesEndPoint;
    private OcorrenciaEndPoint ocorrenciaEndPoint;
    private TipoOcorrenciaEndPoint tipoOcorrenciaEndPoint;
    private AnoLetivoEndPoint anoLetivoEndPoint;
    private SituacaoTurmaMesEndPoint situacaoTurmaMesEndPoint;
    private AlunoNotaMesEndPoint alunoNotaMesEndPoint;

    private BimestreEndPoint bimestreEndPoint;

    private CorpoEndPoint corpoEndPoint;
    private TokenEndPoint tokenEndPoint;

    public APIService(String token) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        this.interceptor = new InterceptorMuralAPI("Bearer " + token);

        OkHttpClient.Builder builderCliente = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(logging);

        builderCliente.interceptors().add(this.interceptor);

        OkHttpClient httpClient = builderCliente.build();

//        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.MINUTES)
//                .readTimeout(10, TimeUnit.MINUTES)
//                .writeTimeout(10, TimeUnit.MINUTES)
//                .addInterceptor(logging)
//                .build();

        Retrofit.Builder builder = new Retrofit.Builder();
        retrofit = builder.baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        turmaEndPoint = retrofit.create(TurmaEndPoint.class);
        funcionarioEndPoint = retrofit.create(FuncionarioEndPoint.class);
        pessoaFisicaEndPoint = retrofit.create(PessoaFisicaEndPoint.class);

        matriculaEndPoint = retrofit.create(MatriculaEndPoint.class);
        alunoFrequenciaMesEndPoint = retrofit.create(AlunoFrequenciaMesEndPoint.class);

        ocorrenciaEndPoint = retrofit.create(OcorrenciaEndPoint.class);
        tipoOcorrenciaEndPoint = retrofit.create(TipoOcorrenciaEndPoint.class);

        anoLetivoEndPoint = retrofit.create(AnoLetivoEndPoint.class);

        situacaoTurmaMesEndPoint = retrofit.create(SituacaoTurmaMesEndPoint.class);
        alunoNotaMesEndPoint = retrofit.create(AlunoNotaMesEndPoint.class);

        bimestreEndPoint = retrofit.create(BimestreEndPoint.class);
        corpoEndPoint = retrofit.create(CorpoEndPoint.class);
        tokenEndPoint = retrofit.create(TokenEndPoint.class);
    }

    public TurmaEndPoint getTurmaEndPoint() {
        return turmaEndPoint;
    }


    public FuncionarioEndPoint getFuncionarioEndPoint() {
        return funcionarioEndPoint;
    }

    public PessoaFisicaEndPoint getPessoaFisicaEndPoint() {
        return pessoaFisicaEndPoint;
    }

    public MatriculaEndPoint getMatriculaEndPoint() {
        return matriculaEndPoint;
    }

    public AlunoFrequenciaMesEndPoint getAlunoFrequenciaMesEndPoint() {
        return alunoFrequenciaMesEndPoint;
    }

    public OcorrenciaEndPoint getOcorrenciaEndPoint() {
        return ocorrenciaEndPoint;
    }

    public TipoOcorrenciaEndPoint getTipoOcorrenciaEndPoint() {
        return tipoOcorrenciaEndPoint;
    }

    public AnoLetivoEndPoint getAnoLetivoEndPoint() {
        return anoLetivoEndPoint;
    }

    public SituacaoTurmaMesEndPoint getSituacaoTurmaMesEndPoint() {
        return situacaoTurmaMesEndPoint;
    }

    public AlunoNotaMesEndPoint getAlunoNotaMesEndPoint() {
        return alunoNotaMesEndPoint;
    }

    public BimestreEndPoint getBimestreEndPoint() {
        return bimestreEndPoint;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public CorpoEndPoint getCorpoEndPoint() {
        return corpoEndPoint;
    }

    public TokenEndPoint getTokenEndPoint() {
        return tokenEndPoint;
    }
}
