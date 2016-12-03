Kelime Zinciri Algoritmasıyla Türkçe Metin Özetleme
===================
---
### Algoritma
> **Kelime Zinciri Algoritması:**

Bu algoritma metnin ana konusunu belirmeye çalışmaktadır. Metindeki kelimeler arasında "anlamsal" bağ kurup, aynı anlama gelen kelimelerden bir zincir oluşturulmaktadır [9]. Daha sonra belirlenen puanlama ve sezgisel yöntemlere göre özet niteliği taşıyabilecek cümleleri seçerek belirlemektedir. Bu algoritma için en önemli şey güçlü bir "kelime ağının" var olmasıdır. Çünkü tüm zincirler kelimeler arasındaki ilişkilerden yola çıkarak oluşturulacağı için, güçlü bir kelime ağı belirsiz kalacak kelime sayısını azaltacak, güçlü zincirler kurulmasını sağlayacaktır. Türkçe için hazırlanmış tek kelime ağı Dr. Özlem Çetinoglu ve Dr. Kemal Oflazer tarafından 2004 yılında “BalkaNet” projesiyle oluşturulmuştur [10]. Tarafımızdan geliştirilecek bu proje kapsamında, wordnetin javaya aktarılması ve kullanılabilir hale getirilmesi gerekmekte idi. Öncelikle XML halinde bulunan bu listeyi çözümleyerek (parsing) yeniden oluşturulmuştur. Kullanılacak formata dönüşüm gerçekleştirilmiştir. Daha sonra Yıldız Teknik Üniversitesi Bilgisayar Mühendisliği bölümünden Emre Yıldız'ın oluşturduğu “Anlamsal İlişkiler Veri Kümesi” projesi dökumanları da kullanılacak formata dönüştürülmüştür [11]. Bu iki listeyi birleştirip kendi projemiz için ortak bir kelime ağı oluşturduk. Bu kelime ağı içindeki verileri ön aşamadan geçirdik, bu aşamalar;
>- Etkisiz kelimeler temizlendi.
>- Atasözler ve deyimler çıkartıldı.
>- Hyponymy ilişkiler, hypernymy ilişkilere dönüştürüldü. (Çünkü bu şekilde kelimelerin aranması kolaylaşmaktadır.)
>- Sıfatlar ve fiiller çıkartıldı.
>- Yer belirten adlar düzenlendi.
>- Terim listeleri eklendi.
>- Sayılar çıkartıldı.

Uygulamamızda tüm bu listeyi okuyup her kelime için bir ilişki listesi oluşturuyoruz, bir kelime girildiğinde bunun ilişkili olduğu kelimeler liste halinde kullanıcıya gönderilmektedir. Bu sistemin ileride ayrı bir servis olarak kullanıma sunulması planlanmaktadır. Böylece çevrimiçi Türkçe kelime ağı erişime açılmış olacaktır.

> **Zincirlerin puanlanması:**
> 
Bu aşamada zincirdeki kelimelerin aralarındaki ilişkiye göre puanlamasını gerçekleştirdik. Regina Barzilay tarafından hazırlanmış olan "Using Lexical Chains for Text Summarization [13]" doktora tezinden ve "Assessing the Impact of Lexical Chain Scoring Methods and Sentence Extraction Schemes on Summarization [14]" makalesinden faydalanılmıştır. Oluşturdukları puanlama sistemi kendi uygulamamıza göre değiştirilmiştir. Aşağıdaki şekilde bir puanlama sistemi oluşturulmuştur:

>- synonymy = 10
>- antonymy = 7
>- hypernymy = 4
>- hyponymy = 4
>- related_with =4
>- holo_part = 4
>- holo_member = 4
>- yan_kavram = 4

Ayrıca zincirlerin gücünü belirlemek için ayrı bir sistem daha kullanılmıştır. Bu sistem şu
aşamalardan oluşmaktadır:
>- Zincirdeki benzersiz kelimelerin sayısını bul
>- Homojenlik değerini bul = 1 - (benzersiz kelime sayısı / tüm kelimelerin sayısı)
>- Eşik değerini bul = ortalama puan + (2 ∗ puanların standart sapması)
>- Eşik değerinin üstündeki zincirleri güçlü zincirler olarak listeye al
###### 
> **Cümle Seçimi İşlemleri:**

>- Sezgisel Algoritma 1
Bu algoritma her zincirdeki ilk kelimenin yani zinciri başlatan kelimenin anahtar kelime olarak alınmasına dayanmaktadır. Bu anahtar kelimenin geçtiği ilk cümle tespit edilerek seçilmiştir.
>- Sezgisel Algoritma 2
Bu algoritma her zincirdeki kelimelerin frekansının hesaplanmasına dayanmaktadır. Öncelikle tüm kelimelerin frekansı hesaplanır ve zincirdeki kelimelerin frekans ortalaması bulunur. Ortalama frekansın üstündeki kelimeler işleme alınır. Bu kelimelerin ortak olarak geçtikleri bir cümle mevcut ise bu cümle seçilir, eğer hiçbir kelimenin kesiştiği bir cümle yoksa, en yüksek frekanslı cümlenin geçtiği cümle alınır.
>- Sezgisel Algoritma 3
Bu algoritma her zincirin yoğunlaştığı paragrafı bulmaya ve bu paragrafta zincirdeki
kelimeleri içeren cümle frekansına dayanmaktadır. Eğer tüm kelimeler aynı paragraftaysa doğrudan bu paragraftaki cümlelerin analizi yapılır. Eğer zincirdeki kelimeler farklı paragraflardaysa öncelikle paragrafların frekansları alınır. En yüksek frekanslı paragraf seçilir ve bu paragraftaki cümlelerin analizi yapılır. Cümle analizi, öncelikle zincirdeki kelimelerin hangi cümlelerde geçtiği bilindiği için bu kelimeler paragraflara göre ayıklanır ve her paragraf içinde bu kelimelerin ait oldukları cümlelerin frekansı alınır. En yüksek frekanslı cümleyi o zincir için seçilmektedir.

[10] Stamou, S., Oflazer, K., Pala, K., Christodoulakis, D., Cristea, D., Tufis, D., Koeva, S.,Totkov,G., Dutoit, D., Grigoriadou, M.: Balkanet: A multilingual semantic network for Balkan languages.Proceedings of the 1st Global Wordnet Conference. Mysore, Hindistan, (2002).
[11] Emre Yıldız, “Anlamsal İlişkiler Veri Kümesi”, Yıldız Teknik Üniversitesi, Bilgisayar Müh.
Bölümü,(2010).
[12] Oğuz Yıldırım, Fatih Atık, M. Fatih AMASYALI, "42 Bin Haber Veri Kümesi”, Yıldız Teknik
Üniversitesi, Bilgisayar Müh. Bölümü,(2003).
[13] Regina Barzilay and Michael Elhadad, “Using Lexical Chains for Text Summarization”, In
Proceedings of the ACL Workshop on Intelligent Scalable Text Summarization,(1997), 10-17.
[14] William Doran, Nicola Stokes, Joe Carthy, John Dunnion. "Assessing the Impact of Lexical Chain Scoring Methods and Sentence Extraction Schemes on Summarization",
Computational Linguistics and Intelligent Text Processing Volume 2945 of the series Lecture Notes in Computer Science , (2004), 627-635.

---
###Kurulum

    $ git clone https://github.com/teaddict/turkce-metin-ozetleme

Projeyi kopyaladiktan sonra [Eclipse Java EE IDE for Web Developers](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/indigosr2) ile "Importing existing projects" kullanarak projeyi calistirabilirsiniz. Maven kutuphaneleri kullanildigi icin 
>- Projeye sag tiklayip -> Maven -> Update Maven project -> Force update of Snapshots/Releases sec -> ok butonuna tiklayip beklerseniz tum maven kutuphanelerini yukleyecektir
##### 
> **Gereksinimler:** 

>- JavaSE 1.8
>- Apache Tomcat v8.0
>- Ubuntu
>- PostgreSQL

> **DB kurulumu :** 

>- Öncelikle postgresql için user oluşturuyoruz sonra veritabanını oluşturuyoruz, user şifre olarak"turkishtext" verdim, suan programda default olan bu, eğer farklı bi şifre girerseniz programda da değiştirmeniz gerekiyor!
><b>sudo -u postgres createuser -D -A -P textsummarizer
>sudo -u postgres createdb -O textsummarizer summarydb</b>
>- postgresql komutunu girip daha "psql" komutunu girdikten sonra sırasıyla aşağıdaki komutları girmeniz gerekiyor
><b>CREATE DATABASE summarydb;
>CREATE TABLE public.summary
(
	  id BIGINT PRIMARY KEY NOT NULL,
	  context_of_text text NOT NULL,
	  summary_of_text text NOT NULL,
	  word_chain text,
	  filename character varying(255) DEFAULT NULL::character varying,
	  class_of_text character varying(255) DEFAULT NULL::character varying,
	  class_of_summary character varying(255) DEFAULT NULL::character varying
);

</b>

----------

###Kullanim

com.summarizer.main paketi altındaki sınıfların kısaca kullanım amaçları
> **NewPreprocess:** 
> 
>- Metin Okuma
>- Metin Temizleme
>- İsim Bulma
>- Fiil Bulma
>- Paragraflara Ayırma
>- Cümlelere Ayırma
>- Özet Çıkarma
>- Metin Sınıflandırma

> **NewWordnetParser:** 
> 
>-  Dr. Kemal Oflazer ve Dr. Özlem Çetinoğlu tarafından 2004 yılında Türkçe için hazırlanmış kelime ağını kullanabilmek için yazdığımız parser.

> **NewExtractKeywords:**

>- Belirlediğiniz klasör içindeki tüm text dosyalarını okur kelimelere ayırır ve bir liste çıkartır <b>csv</b> dosyası olarak kaydeder. Siz ister ofis uygulamanızla ister python scriptleriyle bu liste üzerinde çalışabilir, çıkarımlar yapabilirsiniz.

> **NewLearner && NewClassifier:**
>
>- Sınıflandırma işlemleri için kullandığımız sınıflar, biz bunu haber veriseti için uygun şekilde düzenledik.

----------

###Örnek

> **Haber metni:**

﻿Yıldız Kızlarımız Dünya Şampiyonu
﻿
Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu. Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti.

Yıldız Milli Takım, TVF Başkent Salonu'nda yapılan final maçında baştan sona üstün bir performans sergileyerek, Dünyanın en iyi takımları arasında yer alan Çin'e adeta göz açtırmadı. Tüm oyuncuların iyi oynadığı Türk Milli Takımı'nda Kübra Akman performansıyla göz doldururken, Çin Milli Takımı'nın solak smaçörü Peiyi Liu, Yıldız kızları zorlayan en önemli oyuncu oldu. Türkiye, 2007 yılında Meksika'da yapılan Dünya Yıldız Kızlar Şampiyonası finalinde Çin'e karşı 3-1 kaybederek Dünya ikincisi olduğu maçın rövanşını set kayıpsız aldı.

Bu arada karşılaşmayı Gençlik ve Spor Bakanı Suat Kılıç, Türkiye Voleybol Federasyonu Başkanı Erol Ünal Karabıyık ile birlikte protokol tribününden takip etti. TVF Başkent Salonu'nun tamamını dolduran seyirciler, ellerindeki Türk bayraklarıyla maç boyunca Türk Milli Takımı'nı coşkulu bir şekilde desteklediler.Voleybolseverler, TVF Bandosunun çaldığı hareketli parçalara eşlik ederek, takımlarını bir an bile yalnız bırakmadılar.

Yıldız Kızlar Dünya Şampiyonası FIVB'nin düzenlediği ve 18 yaşının altındaki voleybolcuların katılabildiği bir şampiyonadır.  İlk şampiyona 1989 yılında Brezilya'nın Curitiba kentinde yapılmıştır. Her iki yılda bir düzenlenen şampiyonaya kıta elemelerini geçen ülke takımları katılabilmektedir.

> **Özet:**
[Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu., Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti., Yıldız Milli Takım, TVF Başkent Salonu'nda yapılan final maçında baştan sona üstün bir performans sergileyerek, Dünyanın en iyi takımları arasında yer alan Çin'e adeta göz açtırmadı.] 

> **Kelime Zincirleri**
>"P1 S2" kelimenin 1. paragrafdaki 2. cümlede  geçtiğini belirtir
>
>- (voleybol related_with spor) P0-S0,(final related_with spor) P0-S0,(final related_with spor) P1-S1,(takım related_with spor) P1-S1,(oyun related_with spor) P1-S2,(takım related_with spor) P1-S2,(takım related_with spor) P1-S2,(smaçör related_with spor) P1-S2,(oyun related_with spor) P1-S2,(voleybol related_with spor) P2-S3,(takım related_with spor) P2-S4,(voleybol related_with spor) P3-S5,
>Zincirdeki kelime sayisi: 12
>Zincirin iliskisel puan degeri: 48
>Zincirin guc degeri: 6.999999999999999
>-     (yıldız hypernymy astronomi) P0-S0,(yıldız holo_member astronomi) P0-S0,(yıldız hypernymy astronomi) P0-S0,(yıldız holo_member astronomi) P0-S0,(yıldız hypernymy astronomi) P1-S1,(yıldız holo_member astronomi) P1-S1,(yıldız hypernymy astronomi) P1-S2,(yıldız holo_member astronomi) P1-S2,(yıldız hypernymy astronomi) P3-S5,(yıldız holo_member astronomi) P3-S5,(yıl related_with astronomi) P3-S6,:11:44:9.0
>Zincirdeki kelime sayisi: 11
>Zincirin iliskisel puan degeri: 44
>Zincirin guc degeri: 9.0
>-     (takım related_with biyoloji) P1-S1,(takım holo_member biyoloji) P1-S1,(takım related_with biyoloji) P1-S2,(takım holo_member biyoloji) P1-S2,(takım related_with biyoloji) P1-S2,(takım holo_member biyoloji) P1-S2,(takım related_with biyoloji) P2-S4,(takım holo_member biyoloji) P2-S4,:8:32:7.0
>Zincirdeki kelime sayisi: 8
>Zincirin iliskisel puan degeri: 32
>Zincirin guc degeri: 7.0
>- (yıldız synonymy takımyıldız) P0-S0,(yıldız holo_member takımyıldız) P0-S0,(yıldız synonymy takımyıldız) P0-S0,(yıldız holo_member takımyıldız) P0-S0,(yıldız synonymy takımyıldız) P1-S1,(yıldız holo_member takımyıldız) P1-S1,(yıldız synonymy takımyıldız) P1-S2,(yıldız holo_member takımyıldız) P1-S2,(yıldız synonymy takımyıldız) P3-S5,(yıldız holo_member takımyıldız) P3-S5,:10:70:9.0
>Zincirdeki kelime sayisi: 10
>Zincirin iliskisel puan degeri: 70
>Zincirin guc degeri: 9.0
>
>- <b> TOPLAM SONUCLAR</b>
>Tüm zincirler: 306
Benzersiz zincirler: 73
Güçlü zincirler: 4
Kelime zinciri ortalama puan değeri:17.904109589041095
Kelime zinciri ortalama güç değeri:1.2602739726027397
Kelime zinciri kriter değeri:5.500038727775852

> **Sinif:**
> 
>- Orjinal metin: <b>spor</b>
>- Özet metin: <b>spor</b>

> **JSON API RESPOND:**
>- {
>   <b>"summaryId"</b>:88,
>   <b>"contextOfText"</b>:"﻿Yıldız Kızlarımız Dünya Şampiyonu\r\n\r\nDünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu. Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti.\r\n\r\nYıldız Milli Takım, TVF Başkent Salonu'nda yapılan final maçında baştan sona üstün bir performans sergileyerek, Dünyanın en iyi takımları arasında yer alan Çin'e adeta göz açtırmadı. Tüm oyuncuların iyi oynadığı Türk Milli Takımı'nda Kübra Akman performansıyla göz doldururken, Çin Milli Takımı'nın solak smaçörü Peiyi Liu, Yıldız kızları zorlayan en önemli oyuncu oldu. Türkiye, 2007 yılında Meksika'da yapılan Dünya Yıldız Kızlar Şampiyonası finalinde Çin'e karşı 3-1 kaybederek Dünya ikincisi olduğu maçın rövanşını set kayıpsız aldı.\r\n\r\nBu arada karşılaşmayı Gençlik ve Spor Bakanı Suat Kılıç, Türkiye Voleybol Federasyonu Başkanı Erol Ünal Karabıyık ile birlikte protokol tribününden takip etti. TVF Başkent Salonu'nun tamamını dolduran seyirciler, ellerindeki Türk bayraklarıyla maç boyunca Türk Milli Takımı'nı coşkulu bir şekilde desteklediler.Voleybolseverler, TVF Bandosunun çaldığı hareketli parçalara eşlik ederek, takımlarını bir an bile yalnız bırakmadılar.\r\n\r\nYıldız Kızlar Dünya Şampiyonası FIVB'nin düzenlediği ve 18 yaşının altındaki voleybolcuların katılabildiği bir şampiyonadır.  İlk şampiyona 1989 yılında Brezilya'nın Curitiba kentinde yapılmıştır. Her iki yılda bir düzenlenen şampiyonaya kıta elemelerini geçen ülke takımları katılabilmektedir.",
>  <b>"summaryOfText"</b>:"[Dünya Yıldız Kızlar Voleybol Şampiyonası'nda Yıldız Milli Takım, final maçında Çin'i 3-0 yenerek şampiyon oldu., Türkiye, böylece voleybol tarihinin ilk Dünya şampiyonluğunu elde etti., Yıldız Milli Takım, TVF Başkent Salonu'nda yapılan final maçında baştan sona üstün bir performans sergileyerek, Dünyanın en iyi takımları arasında yer alan Çin'e adeta göz açtırmadı.]",
>   "<b>wordChain</b>":null,
>   "<b>filename</b>":null,
>   "<b>classOfText</b>":"spor",
>   "<b>classOfSummary</b>":"spor"
}

----------

###API Kullanım

>- [teaddict.net/ozetle](teaddict.net/ozetle) adresinden özetleme işlemini browser üzerinden yapabilirsiniz.
>- [teaddict.net/ozetle/api/new](teaddict.net/ozetle) adresine POST methoduyla özetlemek istediğiniz metni ekleyerek istekte bulunursanız JSON formatında cevap alabilirsiniz. Örnek kullanım için [bu dokümanları](https://github.com/teaddict/turkce-metin-ozetleme/tree/master/ornek-api-kullanim) inceleyebilirsiniz.
>- [örnek metin dosyaları](https://github.com/teaddict/turkce-metin-ozetleme/tree/master/ornek-metinler)
