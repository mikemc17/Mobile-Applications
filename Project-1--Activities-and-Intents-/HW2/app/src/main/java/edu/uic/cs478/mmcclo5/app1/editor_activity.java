package edu.uic.cs478.mmcclo5.app1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by Mike on 2/2/2017.
 */

public class editor_activity extends AppCompatActivity {

    String tag = "getNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        final Button getNum = (Button) findViewById(R.id.getNumber);
        final EditText text = (EditText) findViewById(R.id.editText);


        getNum.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String input;
                String num = " ",num2 = " ",return_num = " ";
                boolean is_num = false, is_num2 = false;

                try
                {
                    input = text.getText().toString();

                     if(input.length() >= 8)
                     {
                         if(input.length() >= 13)
                            num2 = parseFor10DigitNumber(input);

                         num = parseTextFor7DigitNumber(input);

                         if (!num2.equals("")) {
                             is_num2 = true;
                             Intent dial = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num2, null));
                             startActivity(dial);
                         }
                         else if (!num.equals("")) {
                             is_num = true;
                             Intent dial = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null));
                             startActivity(dial);
                         }

                     }

                    if (is_num2)
                        return_num = "Number Found: " + num2;
                     else if (is_num)
                       return_num = "Number Found: " + num;
                     else
                        return_num = "No Number Found.";

                    Intent result = new Intent();
                    result.setData(Uri.parse(return_num));
                    setResult(RESULT_OK,result);

                }
                catch (Exception e)
                {
                    // Log any error messages to LogCat using Log.e()
                    Log.e(tag, e.toString());
                }

            }
        });

    }


    // method to check the text for a phone number without an area code
    public String parseTextFor7DigitNumber(String input)
    {
        Boolean phone_num_found = false;
        String number = "";
        String temp;
        char[] text = input.toCharArray();
        char dash = '-';
        int i,j,num = 0;
        int length = text.length;

        for(i=0;i<length-7;i++) {
            num = 0;
            j = i;

            while (Character.isDigit(text[j])) {
                num++;
                temp = Character.toString(text[j]);
                number = number + temp;

                if (num == 8) {
                    phone_num_found = true;
                    break;
                }

                if ((num == 3) && (text[j + 1] == dash)) {
                    num++;
                    number = number + "-";
                    j += 2;
                } else
                    j++;
            }

            if (phone_num_found)
            {
                break;
            }
            else
            {
                temp = "";
                number = "";
            }

        }

        return number;
    }


    // method to check the text for a phone number with an area code
    public String parseFor10DigitNumber(String input)
    {
        String number = "";
        String temp = "";
        String code1 = "", code2 = "";
        String result = "",result2 = "";
        char[] text = input.toCharArray();
        char[] symbol = {'(', ')', ' '};
        int i, j, num = 0;
        int length = text.length;
        String input2,input3;
        String return_num = "";
        Boolean phone_num_found = false;
        Boolean code1_found = false, code2_found = false;

        for(i=0;i<length-12;i++)
        {
            num = 0;
            j = i;

            if(text[j] == symbol[0])
            {
                num++;
                number = number +Character.toString(text[j]);
                j++;
                while (Character.isDigit(text[j]))
                {
                    num++;
                    number = number +Character.toString(text[j]);

                    if(num == 4)
                    {
                        if(text[j+1] == symbol[1])
                        {
                            code1_found = true;
                            code1 = input.substring(j-3,j+2);

                            if ( (text[j + 2] == symbol[2]))
                            {
                                code2_found = true;
                                code2 = input.substring(j-3,j+3);
                            }
                        }

                        if(code1_found || code2_found)
                            break;
                    }
                    j++;
                }

            }

            if(code1_found)
            {
                number = number + symbol[1];
                input2 = input.substring(j + 2);
                result = parseTextFor7DigitNumber(input2);
            }

            if(code2_found)
            {
                temp = number + symbol[2];
                input3 = input.substring(j + 3);
                result2 = parseTextFor7DigitNumber(input3);
            }

             if(!result.equals(""))
             {
                 return_num = code1 + result;
                 phone_num_found = true;
             }
            else if(!result2.equals(""))
             {
                 return_num = code2 + result2;
                 phone_num_found = true;
             }
            else
                return_num = "";

            if (phone_num_found)
            {
                break;
            }
            else
            {
                temp = "";
                number = "";
            }

        }

        return return_num;
    }
}
