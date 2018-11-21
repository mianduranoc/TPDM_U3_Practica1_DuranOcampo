package mx.edu.ittepic.practicau3_1_restaurante_duranocampomiguelangel;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {
    Spinner boxPlatillos,boxBebidas;
    EditText mesa,cantidadPlatillos,cantidadBebidas;
    Button agregarBebida,agregarPlatillo,guardar;
    ListView lista;
    List<Map> platillos,bebidas,listaFinal;
    DatabaseReference basedatos;
    ArrayAdapter<String> adaptador;
    List elementos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        boxBebidas=findViewById(R.id.bebidascombo);
        boxPlatillos=findViewById(R.id.platilloscombo);
        mesa=findViewById(R.id.nomesa);
        cantidadBebidas=findViewById(R.id.cantidadbebidas);
        cantidadPlatillos=findViewById(R.id.cantidad);
        agregarBebida=findViewById(R.id.agregarBebida);
        agregarPlatillo=findViewById(R.id.cantidadplatillos);
        guardar=findViewById(R.id.guardarPedido);
        lista=findViewById(R.id.listaPedido);
        basedatos=FirebaseDatabase.getInstance().getReference();
        platillos=new ArrayList<>();
        bebidas=new ArrayList<>();
        elementos=new ArrayList();
        listaFinal=new ArrayList<>();
        adaptador=new ArrayAdapter<>(Main3Activity.this,android.R.layout.simple_list_item_1,elementos);
        lista.setAdapter(adaptador);
        basedatos.child("Platillo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cargarListas("Platillo", dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        basedatos.child("Bebida").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cargarListas("Bebida",dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        agregarPlatillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String platillo=boxPlatillos.getSelectedItem().toString();
                int cantidad=Integer.parseInt(cantidadPlatillos.getText().toString());
                elementos.add(platillo+"\n"+cantidad);
                adaptador.notifyDataSetChanged();
                Map<String,Object> dato=new HashMap<>();
                Map<String,Object> datos=new HashMap<>();
                datos.put("platillo",platillo);
                datos.put("cantidad",cantidad);
                float precio=Float.parseFloat(platillos.get(boxPlatillos.getSelectedItemPosition()).get("precio").toString());
                datos.put("total",precio*cantidad);
                dato.put("platillo",datos);
                listaFinal.add(dato);
                boxPlatillos.setSelection(0);
                cantidadPlatillos.setText("");
            }
        });
        agregarBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bebida=boxBebidas.getSelectedItem().toString();
                int cantidad=Integer.parseInt(cantidadBebidas.getText().toString());
                elementos.add(bebida+"\n"+cantidad);
                adaptador.notifyDataSetChanged();
                Map<String,Object> dato=new HashMap<>();
                Map<String,Object> datos=new HashMap<>();
                datos.put("bebida",bebida);
                datos.put("cantidad",cantidad);
                float precio=Float.parseFloat(bebidas.get(boxBebidas.getSelectedItemPosition()).get("precio").toString());
                datos.put("total",precio*cantidad);
                dato.put("bebida",datos);
                listaFinal.add(dato);
                boxBebidas.setSelection(0);
                cantidadBebidas.setText("");
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String []seleccion=elementos.get(position).toString().split("\n");
                AlertDialog.Builder alerta=new AlertDialog.Builder(Main3Activity.this);
                alerta.setTitle("Detalle")
                        .setMessage("Desea eliminar "+seleccion[0]+" del pedido?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               elementos.remove(position);
                               adaptador.notifyDataSetChanged();
                               listaFinal.remove(position);
                               dialog.dismiss();
                            }
                        })
                        .setNegativeButton("NO",null).show();
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object>dato=new HashMap<>();
                dato.put("fecha",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                dato.put("estatus","por pagar");
                dato.put("nomesa",Integer.parseInt(mesa.getText().toString()));
                float total=0;
                String cadPlato,cadBebi;cadPlato="Platillos: ";cadBebi="Bebidas: ";
                for (int i=0;i<listaFinal.size();i++){
                    if (listaFinal.get(i).containsKey("bebida")) {
                        Map<String,Object> datos=(Map)listaFinal.get(i).get("bebida");
                        Log.e("Entro a bebidas",datos.get("bebida").toString());
                        cadBebi+="&"+datos.get("bebida").toString()+",";
                        cadBebi+=datos.get("cantidad").toString()+",";
                        float tot=Float.parseFloat(datos.get("total").toString());
                        total+=tot;
                        cadBebi+=tot;

                    }
                    else if (listaFinal.get(i).containsKey("platillo")) {
                        Map<String,Object> datos=(Map)listaFinal.get(i).get("platillo");
                        Log.e("Entro a platillos",datos.get("platillo").toString());
                        cadPlato+="&"+datos.get("platillo").toString()+",";
                        cadPlato+=datos.get("cantidad").toString()+",";
                        float tot=Float.parseFloat(datos.get("total").toString());
                        total+=tot;
                        cadPlato+=tot;
                    }
                }
                dato.put("platillos",cadPlato);
                dato.put("bebidas",cadBebi);
                dato.put("total",total);
                basedatos.child("Comanda").push().setValue(dato);
                mesa.setText("");
                cantidadBebidas.setText("");
                cantidadPlatillos.setText("");
                boxBebidas.setSelection(0);
                boxPlatillos.setSelection(0);
                lista.setAdapter(null);
                Toast.makeText(Main3Activity.this, "Se ha insertado el pedido correctamente", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void cargarListas(final String alimento, DataSnapshot dataSnapshot) {
        if (dataSnapshot.getChildrenCount()<=0){
            Toast.makeText(Main3Activity.this,"No hay datos que mostrar",Toast.LENGTH_LONG).show();
            return;
        }
        for (final DataSnapshot otro:dataSnapshot.getChildren()){
            basedatos.child(alimento).child(otro.getKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (alimento.equals("Platillo")){
                        Platillo plati=dataSnapshot.getValue(Platillo.class);
                        if (plati!=null){
                            Map<String,Object> xx=new HashMap<>();
                            xx.put("id",otro.getKey());
                            xx.put("nombre",plati.getNombre());
                            xx.put("precio",plati.getPrecio());
                            platillos.add(xx);
                            cargarBox(alimento);
                        }
                    }
                    else if (alimento.equals("Bebida")){
                        Bebida bebi=dataSnapshot.getValue(Bebida.class);
                        if (bebi!=null){
                            Map<String,Object> xx=new HashMap<>();
                            xx.put("id",otro.getKey());
                            xx.put("nombre",bebi.getNombre());
                            xx.put("precio",bebi.getPrecio());
                            bebidas.add(xx);
                            cargarBox(alimento);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void cargarBox(String alimento) {
        if (alimento.equals("Platillo")){
            String []vector=new String[platillos.size()];
            for(int i=0;i< vector.length;i++){
                Map<String,Object> ww=new HashMap<>();
                ww=platillos.get(i);
                vector[i]=ww.get("nombre").toString();
            }
            ArrayAdapter<String> list1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vector);
            boxPlatillos.setAdapter(list1);
        }
        else if (alimento.equals("Bebida")){
            String []vector=new String[bebidas.size()];
            for(int i=0;i< vector.length;i++){
                Map<String,Object> ww=new HashMap<>();
                ww=bebidas.get(i);
                vector[i]=ww.get("nombre").toString();
            }
            ArrayAdapter<String> list1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vector);
            boxBebidas.setAdapter(list1);
        }
    }

    public void onStart(){
        super.onStart();
        cargarBox("Platillo");
        cargarBox("Bebida");
    }


}
