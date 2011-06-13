package com.android.wifibar;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FacturaActivity extends Activity {
   private int nFactura;
   private int mesa;
   private float entregado;
   private float total;
   private float devo;
   public static FacturaHandler factura;
   private EditText entrega;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.factura);

      Bundle bundle = new Bundle();
      bundle = getIntent().getExtras();

      // PROCEDIMIENTO CERRAR MESA
      float total = wifiBarActivity.db.cerrarMesa(bundle.getInt("mesa"));
      this.setTotal(total);

      // Poner atributos
      this.setMesa(bundle.getInt("mesa"));
      this.setnFactura(bundle.getInt("factura"));

      // CREO UNA INSTANCIA DE FACTURA
      factura = new FacturaHandler(bundle.getInt("mesa"),
            bundle.getInt("factura"), this.getTotal());

      // Poner la informacion en los TextView
      TextView tvFac = (TextView) findViewById(R.id.tvFactura);
      TextView tvMesa = (TextView) findViewById(R.id.tvMesaFac);
      TextView tvTotal = (TextView) findViewById(R.id.tvPutTotal);

      tvFac.setText("Factura nº:" + " " + String.valueOf(factura.getnFactura()));
      tvMesa.setText("Mesa nº:" + " " + String.valueOf(factura.getMesa()));

      //FORMATO EUROS
      DecimalFormat Currency = new DecimalFormat("#0.00 €");    
      tvTotal.setText(String.valueOf(Currency.format(total)));
      tvTotal.setText(String.valueOf(total));

      EditText entrega = (EditText) findViewById(R.id.etEntregado);
      entrega.setInputType(InputType.TYPE_CLASS_NUMBER);

      entrega.setOnKeyListener(new OnKeyListener() {

         @Override
         public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                  && (keyCode == KeyEvent.KEYCODE_ENTER)) {
               EditText tv = (EditText) v;
               float entre = Float.parseFloat(tv.getText().toString());
               if (factura.getTotal() < entre) {
                  factura.setEntregado(entre);
                  calculaVuelta();
                  return true;
               } else {
                  Toast.makeText(FacturaActivity.this, "Cantidad Insuficiente",
                        5).show();
                  return false;
               }

            } else
               return false;
         }

      });

   }

   private void calculaVuelta() {
      float vuelt = factura.getEntregado() - factura.getTotal();
      factura.setDevo(vuelt);
      TextView tvuel = (TextView) findViewById(R.id.tvPutDevolucion);
      //FORMATO EUROS
      DecimalFormat Currency = new DecimalFormat("#0.00 €");     
      tvuel.setText(String.valueOf(Currency.format(vuelt)));
   }

   // GETTERS AND SETTERS
   public int getnFactura() {
      return nFactura;
   }

   public void setnFactura(int nFactura) {
      this.nFactura = nFactura;
   }

   public int getMesa() {
      return mesa;
   }

   public void setMesa(int mesa) {
      this.mesa = mesa;
   }

   public float getTotal() {
      return total;
   }

   public void setTotal(float total) {
      this.total = total;
   }

   public float getDevo() {
      return devo;
   }

   public void setDevo(float devo) {
      this.devo = devo;
   }

   public float getEntregado() {
      return entregado;
   }

   public void setEntregado(float entregado) {
      this.entregado = entregado;
   }

}