using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Speech.Synthesis;
using System.Speech.Recognition;
using System.Diagnostics;
using System.IO.Ports;
using System.Xml;
using System.Net;
using System.IO;
 

namespace TalkBack
{
    public partial class Form1 : Form
    {

        //Provides access to the functionality of an installed speech synthesis engine
        //it changes text into voice
        SpeechSynthesizer s = new SpeechSynthesizer(); 
        Boolean wake = true;

        String name = "dipty";
        Boolean[] var1 = new Boolean[2]{false,true};

       // WebClient w = new WebClient();

        String temp, condition;
        Choices list = new Choices(); //Represents a set of alternatives in the constraints of a speech recognition grammar.

        public Boolean search = false;
        public Boolean convert = false;
        public Boolean convert2 = false;

        public Form1()
        {
            SpeechRecognitionEngine rec = new SpeechRecognitionEngine();

           /* list.Add(new String[] { "what time is it", "what day is today", "open google","wake", "sleep","restart","update",  "open codeforces",  "open excel", "close excel", "open word", "close word", "open chrome", "close chrome",
            "whats the weather like","whats the temperature", "how are you","hey you","minimize","unminimize","maximize","mediaplayer","open gmail","open youtube"
            });*/
            list.Add(File.ReadAllLines(@"G:\c#Projetc\voicebotcommmands\commands.txt"));

            Grammar gr = new Grammar(new GrammarBuilder(list));

            try
            {
                rec.RequestRecognizerUpdate();
                rec.LoadGrammar(gr);
                rec.SpeechRecognized += rec_SpeechRecognized;
                rec.SetInputToDefaultAudioDevice();
                rec.RecognizeAsync(RecognizeMode.Multiple); //once you say something it reset you to say something else
            }
            catch
            {
                return;
            }

            s.SelectVoiceByHints(VoiceGender.Female);
            s.Speak("welcome to voice bot!");
            //Process.Start(@"C:\Program Files\Google\Chrome\Application\chrome.exe");
            InitializeComponent();
        }

        //getting the weather update from api
        public String GetWeather(String input)
        {
            String query = String.Format("https://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast where woeid in (select woeid from geo.places(1) where text='dhaka, bd')&format=xml&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
            XmlDocument wData = new XmlDocument();
            try
            {
                wData.Load(query);

            }
            catch
            {
                MessageBox.Show("no internet connection");
                return null;
            }

            XmlNamespaceManager manager = new XmlNamespaceManager(wData.NameTable);
            manager.AddNamespace("yweather", "http://xml.weather.yahoo.com/ns/rss/1.0");

            XmlNode channel = wData.SelectSingleNode("query").SelectSingleNode("results").SelectSingleNode("channel");
            XmlNodeList nodes = wData.SelectNodes("query/results/channel");
            try
            {
                int rawtemp = int.Parse(channel.SelectSingleNode("item").SelectSingleNode("yweather:condition", manager).Attributes["temp"].Value);
                temp = (rawtemp - 32)*5/9+""; //converting farenhiet to celcius
                condition = channel.SelectSingleNode("item").SelectSingleNode("yweather:condition", manager).Attributes["text"].Value;
                
                if (input == "temp")
                {
                    return temp;
                }
              
                if (input == "cond")
                {
                    return condition;
                }
            }
            catch
            {
                return "Error Reciving data";
            }
            return "error";
        }

        public static void killProg(string s)
        {
            System.Diagnostics.Process[] procs = null;

            try
            {
                procs = Process.GetProcessesByName(s);

                Process prog = procs[0];

                if (!prog.HasExited)
                {
                    prog.Kill();
                }
            }
            catch
            {
                //say("Office is not open.");
                MessageBox.Show("it is not open");
            }
            finally
            {
                if (procs != null)
                {
                    foreach (Process p in procs)
                    {
                        p.Dispose();
                    }
                }
            }
            procs = null;
        }

        public void restart()
        {
            Process.Start(@"G:\c#Projetc\VoiceBotExe\TalkBack.exe");
            Environment.Exit(0);
        }

        public void say(String h)
        {
            s.Speak(h);
            textBox2.AppendText(h+ "\n");
        }


        //giving various greetings
        String[] greetings = new String[3] { "whats up","hows it going!","hi, how are you"};

        public String greetings_action()
        {
            Random r = new Random();
                return greetings[r.Next(3)];//picking any greeting randomly from greetings[]
        }
        public Boolean var1_action()
        {
            Random r = new Random();
            return var1[r.Next(2)];
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        public void rec_SpeechRecognized (object sender, SpeechRecognizedEventArgs e)
        {
            String r = e.Result.Text;

            
            if (r == "wake")
            {
                wake = true;
                label3.Text = "Sate: Awake";
            }

            if (r == "sleep")
            {
                wake = false;
                label3.Text = "Sate: Sleep";
            }

            if (search)
            {
                Process.Start("https://www.google.com/search?q="+r);
                search = false;
            }
            if (convert)
            {
                try
                {
                    say(1000.0 * double.Parse(r) + "meter");
                    convert = false;
                }
                catch
                {
                   
                    say("Invalid value");
                    convert = false;
                }
            }
            if (convert2)
            {
                try
                {
                    double a = double.Parse(r);
                    say(a / 1000.0 + "kilometer");
                    convert2 = false;
                }
                catch
                {
                   
                    say("Invalid value");
                    convert2 = false;
                }
            }

            if (wake == true&& search == false)
            {

                if (r == "convert to meter")
                {
                    convert = true;
                }
                if (r == "convert to kilometer")
                {
                    convert2 = true;
                }
                if (r == "search for")
                {
                    search = true;
                }

                if (r == "do i like burger")
                {
                    if (var1_action())
                    {
                        say("no you don't like burger");
                    }
                    if (!var1_action())
                    {
                        say("yes you love burger");
                    }
                    
                }
                if (r == "whats my name")
                {
                    say("your name is "+name);
                }
                if(r == "open youtube")
                {
                    Process.Start("http://www.youtube.com");
                }
                if(r == "open gmail")
                {
                    Process.Start("http://www.gmail.com/");
                }
                
                if (r == "mediaplayer")
                {
                    Process.Start(@"C:\Program Files\VideoLAN\VLC\vlc.exe");
                }

                //unminimizing this programs window
                if (r == "unminimize")
                {
                    this.WindowState = FormWindowState.Normal;
                }
                //maximizing this programs window
                if (r == "maximize")
                {
                    this.WindowState = FormWindowState.Maximized;
                }
                //minimizing this programs window
                if(r == "minimize")
                {
                    this.WindowState = FormWindowState.Minimized;
                }

                  if(r == "hey you")
                {
                    say(greetings_action());
                }
                if (r == "whats the weather like")
                {
                    if (GetWeather("cond") != null)
                    {
                        say("the sky is" + GetWeather("cond") + ".");

                    }
                }
                if (r == "whats the temperature")
                {
                    if (GetWeather("temp") != null)
                    {
                        say("it is" + GetWeather("temp") + "degrees.");
                    }
                  
                }

                if(r=="open chrome" )
                {
                    Process.Start(@"C:\Program Files\Google\Chrome\Application\chrome.exe");
                }
                if (r == "exit chrome")
                {
                    killProg(@"chrome");
                }
                if (r == "open word")
                {
                    Process.Start(@"C:\Program Files\Microsoft Office\Office12\WINWORD.exe");
                }
                if (r == "close word")
                {
                    killProg("WINWORD");
                }
                if (r == "open excel")
                {
                    Process.Start(@"C:\Program Files\Microsoft Office\Office12\EXCEL.exe");
                }
                if (r == "close excel")
                {
                    killProg("EXCEL");
                }

                if (r == "restart" || r == "update")
                {
                    restart();
                }

                //what you say
                /*if (r == "hello")
                {
                    //what computer replies
                    say("hi");
                }*/

                if (r == "what time is it")
                {

                    say(DateTime.Now.ToString("h:mm tt")); //DateTime is a class and we've format it in h:mm tt
                }

                if (r == "what day is today")
                {

                    say(DateTime.Now.ToString("M/d/yyyy"));
                }

                if (r == "how are you")
                {

                    say("fine, and you?");
                }

                if (r == "open google")
                {

                    Process.Start("http://www.google.com");
                }
                if (r == "open codeforces")
                {
                    Process.Start("http://codeforces.com/");
                }

            }
            textBox1.AppendText(r + "\n");
         
            
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}
