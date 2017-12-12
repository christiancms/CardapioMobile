package br.com.ezzysoft.cardapiomobile.fragments;

import android.app.Fragment;
import android.os.Bundle;

import java.io.IOException;

import br.com.ezzysoft.cardapiomobile.dao.Sync;
import br.com.ezzysoft.cardapiomobile.util.exception.ErroSistema;

/**
 * Created by christian on 02/11/17.
 */

public class SyncFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            new Sync(getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ErroSistema erroSistema) {
            erroSistema.printStackTrace();
        }

    }
}
