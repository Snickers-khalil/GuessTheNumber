package com.example.guessthenumber


import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    lateinit var textView : TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //declaration et initialisation des variables
        var isRunning :Boolean=false
        var countDownTimer: CountDownTimer? = null
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        val num = findViewById<EditText>(R.id.num)
        val historique = findViewById<TextView>(R.id.historique)
        val result = findViewById<TextView>(R.id.result)
        val his=findViewById<ScrollView>(R.id.his)
val topScore=findViewById<TextView>(R.id.topScore)
       //sqlite
        var helper=MyDBHelper(applicationContext)

        //vérifier le niveau de joueur
        val radioGroup:RadioGroup=findViewById(R.id.group)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.expert) {
                his.visibility = View.INVISIBLE

            }
            if (checkedId == R.id.deb) {
                his.visibility = View.VISIBLE


            }
        }


        var db=helper.readableDatabase
        fun  afficheScore() {
            var rs = db.rawQuery("SELECT * FROM USERS ORDER BY SCORE DESC", null)
            var ch: String = ""
            for (i in 0..9) {
                if (rs.moveToNext()) {


                    ch += rs.getString(1) + " : " + rs.getString(2) + "\n"

                }
            }
            topScore.setText(ch)
        }
        //end top10
afficheScore()
        //timer
        isRunning=true;
        textView = findViewById(R.id.timer);
        val check = findViewById<Button>(R.id.check)
        val start = findViewById<Button>(R.id.start)
        num.visibility = View.INVISIBLE
        check.visibility = View.INVISIBLE
        textView.visibility = View.INVISIBLE;
        var rnum = 0
        var nbEssai = 0
        var s = "";
        var score = 100;
        start.setOnClickListener {
            if (isRunning){
                countDownTimer?.cancel()
            }
            rnum = Random.nextInt(0, 999);
            num.setText("")
            historique.setText("")
            num.visibility = View.VISIBLE;
            check.visibility = View.VISIBLE;
            textView.visibility = View.VISIBLE;
            input.setText("");
            s="";
            result.setText("");
            if (input.getParent() != null) {
                (input.getParent() as ViewGroup).removeView(input) // <- fix
            }
            // time count down for 30 seconds,
            // with 1 second as countDown interval
            countDownTimer=object : CountDownTimer(60000, 1000) {

                // Callback function, fired on regular interval
                override fun onTick(millisUntilFinished: Long) {
                    textView.setText("" + millisUntilFinished / 1000)

                    //check the entered number*****
                    check.setOnClickListener {

                        val enteredNum = num.text.toString();
                        if (enteredNum.isNullOrEmpty())
                        {
                            Toast.makeText(applicationContext,
                                "Tapez un nombre valide", Toast.LENGTH_SHORT).show()
                        }
                        else{
                        nbEssai++

                        s += enteredNum + "\n";
                        historique.setText(s);



                        if (!enteredNum.equals("") && rnum < Integer.parseInt(enteredNum)) {
                            result.setText("Le nombre est plus petit que!" + enteredNum)
                            num.setText("")
                            Toast.makeText(
                                applicationContext,
                                "resultat!" + rnum,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!enteredNum.equals("") && rnum > Integer.parseInt(enteredNum)) {
                            result.setText("Le nombre est plus grand que!" + enteredNum)
                            num.setText("")
                            Toast.makeText(
                                applicationContext,
                                "resultat!" + rnum,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            score = (score-(nbEssai+(60-(millisUntilFinished/1000)))).toInt()
                            num.setText("")
                            if (!enteredNum.equals("") && (score != 0) && (score > 0)){
                                result.setText("");


                                builder.setTitle("You Win")
                                builder.setMessage("Finished on :"+millisUntilFinished/1000+" second(s) After "+nbEssai+" tentative(s) \n Your score was : "+score)

// Set up the input
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                input.setHint("Enter Text")
                                input.inputType = InputType.TYPE_CLASS_TEXT
                                builder.setView(input)
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                                // Here you get get input text from the Edittext


                               var m_Text = input.text.toString()
                                Toast.makeText(applicationContext,
                                    "your name"+m_Text, Toast.LENGTH_SHORT).show()
                                var cv = ContentValues()

                                cv. put ("UNAME", m_Text)
                                cv.put ("SCORE", score)

                                db. insert ( "USERS", null, cv)
                                afficheScore()

                            }


                            builder.show()




                               /* //set Top10
                                var rs=db.rawQuery("SELECT * FROM USERS ORDER BY SCORE DESC",null)
                                var ch:String=""
                                for (i in 0..10){
                                    if(rs.moveToNext()) {


                                        ch+=rs.getString(1)+" : "+rs.getString(2)+"\n"

                                    }}
                                topScore.setText(ch)*/
                          //  result.setText("C'est 1gangné après" + nbEssai + "tentative(s)\n le nombre cherché est" + enteredNum+"votre ecore "+score)
                           countDownTimer?.cancel()
                                isRunning=false
                            num.setText("")
                            num.visibility = View.INVISIBLE;
                            check.visibility = View.INVISIBLE;
                            nbEssai = 0
                                historique.setText("");
                                textView.visibility=View.INVISIBLE
                        }
                        else{
                                result.setText("you lose")
                                historique.setText("Historique")

                                num.setText("")
                                num.visibility = View.INVISIBLE;
                                check.visibility = View.INVISIBLE;
                                textView.setText("End!")
                                nbEssai = 0
                                result.setText("");

                            }

                        }

                    }
                    }
                }

                // Callback function, fired
                // when the time is up
                override fun onFinish() {
                    isRunning=false

                    result.setText("you lose")
                    historique.setText("Historique")

                    num.setText("")
                    num.visibility = View.INVISIBLE;
                    check.visibility = View.INVISIBLE;
                    textView.setText("End!")
                    nbEssai = 0
                    result.setText("");


                }

            }.start()
        }


        //start code

        //start random number****


    }
}


    //end of oncreate
