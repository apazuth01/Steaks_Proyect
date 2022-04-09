package com.uth.steaks.ui.pedidos;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.uth.steaks.R;


public class FragmentPedidos extends Fragment {

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private PedidosAdaptador pedidosAdaptador;
    private final CollectionReference clt = firebaseFirestore.collection("clt_pedidos");

    @SuppressLint("NotifyDataSetChanged")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);

        SharedPreferences sharedPreferences2 = getContext().getSharedPreferences("getProfile",MODE_PRIVATE);
        String Phone = sharedPreferences2.getString("phone",null);


        Query query = clt
                .whereEqualTo("doc_telefono",Phone)
                .orderBy("doc_fecha",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<PedidosConstructor> firestoreRecyclerOptions
                = new FirestoreRecyclerOptions.Builder<PedidosConstructor>()
                .setQuery(query, PedidosConstructor.class)
                .build();

        pedidosAdaptador = new PedidosAdaptador(firestoreRecyclerOptions,getContext());
        pedidosAdaptador.notifyDataSetChanged();
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerPedidos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(pedidosAdaptador);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        pedidosAdaptador.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        pedidosAdaptador.stopListening();
    }


}