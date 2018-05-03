package com.wuyts.nik.boodschappenlijst.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Veronique Wuyts on 26/04/2018.
 */

public class ShoppingListDbHelper extends SQLiteOpenHelper {
    // Database name and version
    public static final String DATABASE_NAME = "ShoppingList.db";
    public static final int DATABASE_VERSION = 1;

    // SQL statements to create tables
    private static final String SQL_CREATE_SHOP =
        "CREATE TABLE " + shoppingListContract.Shop.TABLE_NAME + " (" +
                shoppingListContract.Shop._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.Shop.COLUMN_NAME_NAME + " TEXT NOT NULL)";
    private static final String SQL_CREATE_CATEGORY =
        "CREATE TABLE " + shoppingListContract.Category.TABLE_NAME + " (" +
                shoppingListContract.Category._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.Category.COLUMN_NAME_NAME + " TEXT NOT NULL)";
    private static final String SQL_CREATE_UNIT =
        "CREATE TABLE " + shoppingListContract.Unit.TABLE_NAME + " (" +
                shoppingListContract.Unit._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.Unit.COLUMN_NAME_NAME + " TEXT NOT NULL)";
    private static final String SQL_CREATE_SHOPPING_LIST =
        "CREATE TABLE " + shoppingListContract.List.TABLE_NAME + " (" +
                shoppingListContract.List._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.List.COLUMN_NAME_NAME + " TEXT NOT NULL, " +
                shoppingListContract.List.COLUMN_NAME_IS_RECIPE + " INTEGER NOT NULL DEFAULT 0)";
    private static final String SQL_CREATE_SHOPPING_ORDER = "CREATE TABLE " +
            shoppingListContract.ShoppingOrder.TABLE_NAME + " (" +
            shoppingListContract.ShoppingOrder._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            shoppingListContract.ShoppingOrder.COLUMN_NAME_CATEGORY_ID + " INTEGER NOT NULL, " +
            shoppingListContract.ShoppingOrder.COLUMN_NAME_SHOP_ID + " INTEGER NOT NULL, " +
            shoppingListContract.ShoppingOrder.COLUMN_NAME_SEQUENCE + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + shoppingListContract.ShoppingOrder.COLUMN_NAME_CATEGORY_ID +
            ") REFERENCES " + shoppingListContract.Category.TABLE_NAME + "(" + shoppingListContract.Category._ID + "), " +
            "FOREIGN KEY(" + shoppingListContract.ShoppingOrder.COLUMN_NAME_SHOP_ID +
            ") REFERENCES " + shoppingListContract.Shop.TABLE_NAME + "(" + shoppingListContract.Shop._ID + "))";
    private static final String SQL_CREATE_PRODUCT =
        "CREATE TABLE " + shoppingListContract.Product.TABLE_NAME + " (" +
                shoppingListContract.Product._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.Product.COLUMN_NAME_CATEGORY_ID + " INTEGER NOT NULL, " +
                shoppingListContract.Product.COLUMN_NAME_SHOP_ID + " INTEGER, " +
                shoppingListContract.Product.COLUMN_NAME_UNIT_ID + " INTEGER, " +
                shoppingListContract.Product.COLUMN_NAME_NAME + " TEXT NOT NULL," +
                shoppingListContract.Product.COLUMN_NAME_IMAGE + " BLOB, " +
                shoppingListContract.Product.COLUMN_NAME_NOTE + " TEXT, " +
                shoppingListContract.Product.COLUMN_NAME_FIXED_SHOP + " INTEGER NOT NULL DEFAULT 0, " +
                shoppingListContract.Product.COLUMN_NAME_FAVORITE + " INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(" + shoppingListContract.Product.COLUMN_NAME_CATEGORY_ID +
                    ") REFERENCES " + shoppingListContract.Category.TABLE_NAME + "(" + shoppingListContract.Category._ID + "), " +
                "FOREIGN KEY(" + shoppingListContract.Product.COLUMN_NAME_SHOP_ID +
                    ") REFERENCES " + shoppingListContract.Shop.TABLE_NAME + "(" + shoppingListContract.Shop._ID + "), " +
                "FOREIGN KEY(" + shoppingListContract.Product.COLUMN_NAME_UNIT_ID +
                    ") REFERENCES " + shoppingListContract.Unit.TABLE_NAME + "(" + shoppingListContract.Unit._ID + "))";
    private static final String SQL_CREATE_ITEM_ON_LIST =
        "CREATE TABLE " + shoppingListContract.ItemOnList.TABLE_NAME + " (" +
                shoppingListContract.ItemOnList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                shoppingListContract.ItemOnList.COLUMN_NAME_SHOPPING_LIST_ID + " INTEGER NOT NULL, " +
                shoppingListContract.ItemOnList.COLUMN_NAME_PRODUCT_ID + " INTEGER NOT NULL, " +
                shoppingListContract.ItemOnList.COLUMN_NAME_AMOUNT + " INTEGER, " +
                shoppingListContract.ItemOnList.COLUMN_NAME_PROMOTION + " INTEGER NOT NULL DEFAULT 0, " +
                shoppingListContract.ItemOnList.COLUMN_NAME_BOUGHT + " INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(" + shoppingListContract.ItemOnList.COLUMN_NAME_SHOPPING_LIST_ID +
                ") REFERENCES " + shoppingListContract.List.TABLE_NAME + "(" + shoppingListContract.List._ID + "), " +
                "FOREIGN KEY(" + shoppingListContract.ItemOnList.COLUMN_NAME_PRODUCT_ID +
                ") REFERENCES " + shoppingListContract.Product.TABLE_NAME + "(" + shoppingListContract.Product._ID + ")) ";

    // Arrays for inserting data into tables
    private static final String[] SHOP_DATA = {"Aldi", "Bio-Planet", "Carrefour", "Colruyt",
            "Delhaize", "Lidl", "Okay", "Spar"};
    private static final String[] CATEGORY_DATA = {"Fruit", "Groenten", "Beenhouwerij", "Vis",
            "Kaas", "Zuivel", "Pasta-Granen", "Ontbijt", "Bakkerij", "Specerijen",
            "Conserven", "Sauzen", "Diepvries"};
    private static final String[] UNIT_DATA = {"g", "kg", "Stuk", "Zakje", "Bakje", "Pot", "Tros",
            "Bussel", "Kist", "Karton"};
    private static final String[][] PRODUCT_DATA = {
            {"Banenen", "Appels", "Peren", "Druiven", "Pruimen", "Citroen", "Sinaasappel", "Ananas",
                "Perziken", "Nectarines", "Abrikozen", "Kersen", "Mandarijn", "Clementines",
                "Limoen", "Pompelmoes", "Kruisbessen", "Frambozen", "Blauwbessen", "Kiwi"},
            {"Sla", "Tomaten", "Uien", "Wortelen", "Avocado", "Champignons", "Spinazie", "Venkel",
                "Koolrabi", "Aardperen", "Aardapelen", "Aubergine", "Courgette", "Komkommer",
                "Knoflook", "Prei", "Radijsjes", "Rode biet", "Maïs", "Pompoen", "Gele paprika",
                "Groene paprika", "Rode paprika", "Paprika", "Asperges", "Groene Asperges",
                "Selder", "Witte selder", "Groene selder"},
            {"Biefstuk", "Varkenslapje", "Kalfslapje", "Kippenfilet", "Lamsrinstuk", "Konijn",
                "Paardensteak", "Kalkoenfilet", "Gehakt", "Eendenborst", "Cordon blue", "Worst",
                "Chipolata", "Kippenworst", "Schnitzel", "Kippenschnitzel", "Saté"},
            {"Kabeljauw", "Zeebaars", "Makreel", "Gerookte makreel", "Zalm", "Gerookte zalm",
                "Forel", "Tong", "Maatjes", "Tonijn", "Rog", "Garnalen", "Kreeft", "Mosselen",
                "Oesters", "Zeewolf", "Haring", "Brasem", "Schol"},
            {"Jonge kaas sneden", "Jonge kaas blok", "Belegen kaas sneden", "Belegen kaas blok",
                "Oude kaas sneden", "Oude kaas blok", "Geitenkaas", "Kruidenkaas", "Smeerkaas",
                "Feta", "Mozzarella", "Ricotta", "Mascarpone", "Geraspte kaas", "Verse kaas",
                "Parmezaan", "Roquefort", "Brie", "Gouda", "Beemster", "Gruyère", "Emmental",
                "Comté", "Gorgonzola"},
            {"Eieren", "Hoeveboter", "Smeerboter", "Bakboter", "Melk", "Halfvolle melk",
                "Magere melk", "Volle melk", "Sojamelk", "Havermelk", "Amandelmelk", "Chocomelk",
                "Rijstmelk", "Karnemelk", "Room", "Culinaire room", "Slagroom", "Sojaroom",
                "Zure room", "Yoghurt", "Magere yoghurt", "Volle yoghurt", "Fruityoghurt",
                "Griekse yoghurt", "Drinkyoghurt", "Rijstpap", "Pudding", "Chocomousse", "Tiramisu"},
            {"Pasta", "Rijst", "Couscous", "Gnocchi", "Tarwe", "Quinoa", "Bulgur", "Noedels",
                "Spaghetti", "Cappellini", "Lasagne", "Tagliatelle", "Penne", "Basmatirijst",
                "Volle rijst", "Rode rijst", "Arborio rijst", "Boekweit"},
            {"Choco", "Confituur", "Hagelslag", "Honing", "Muesli", "Cruesli", "Corn flakes",
                "Rijstwafels", "Maïswafels", "Beschuiten", "Pindakaas", "Speculaaspasta",
                "Aardbeienconfituur", "Abrikozenconfituur"},
            {"Stokbrood", "Houthakkersbrood", "Witbrood", "Volkorenbrood", "Boerenbrood",
                "Speltbrood", "Roggebrood", "Meergranenbrood", "Sandwich", "Pistolets", "Picolo",
                "Ciabatta", "Foccacia", "Rozijnenbrood", "Suikerbrood", "Fruittaart", "Appeltaart",
                "Aardbeientaart", "Donuts", "Oliebol", "Eclair"},
            {"Zout", "Witte peper", "Zwarte peper", "Roze peper", "Oregano", "Basilicum", "Dille",
                "Cayennepeper", "Chilipeper", "Kippenkruiden", "Stoofvleeskruiden", "Thijm",
                "Rozemarijn", "Cajun", "Kerrie", "Kurkuma", "Kaneel", "Jeneverbessen", "Muskaatnoot",
                "Bieslook", "Saffraan", "Vanillestokjes", "Komijn", "Koriander", "Paprikapoeder"},
            {"Rode bonen", "Borlottibonen", "Tomatenblokjes", "Tomatenpuree", "Augurken",
                "Kappertjes", "Ajuintjes", "Palmharten", "Fruitsalade", "Perziken", "Appelmoes",
                "Veenbessen", "Tonijn", "Makreel", "Ansjovis", "Sardienen", "Zalm", "Ravioli",
                "Witte bonen in tomatensaus", "Cassoulet"},
            {"Mayonaise", "Ketchup", "Mosterd", "Tartaar", "Cocktail", "Andalouse", "Vinaigrette",
                "Pesto", "Sojasaus", "Tabasco", "Worcestersauce", "Bearnaise", "Pickles",
                "Bolognese", "Champignonsaus", "Currysaus", "Provencaalse saus"},
            {"Fishsticks", "Erwten", "Spinazie","Roomijs", "Vanille-ijs", "Chocolade-ijs",
                "Mokka-ijs", "Sorbet", "Citroensorbet", "Garnalen", "Frieten", "Loempia's",
                "Waterijsjes", "Frisco's", "Pizza", "Kipnuggets", "Bosvruchten", "Frambozen",
                "Kroketten", "Zeevruchten"}
    };

    // Array for storing category IDs
    private static long[] categoryIDs = new long[CATEGORY_DATA.length];

    // Constructor
    public ShoppingListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(SQL_CREATE_SHOP);
        db.execSQL(SQL_CREATE_CATEGORY);
        db.execSQL(SQL_CREATE_UNIT);
        db.execSQL(SQL_CREATE_SHOPPING_LIST);
        db.execSQL(SQL_CREATE_SHOPPING_ORDER);
        db.execSQL(SQL_CREATE_PRODUCT);
        db.execSQL(SQL_CREATE_ITEM_ON_LIST);

        // Insert data into tables
        insertShops(db);
        insertCategory(db);
        insertUnit(db);
        insertList(db);
        insertProduct(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO: implement onUpgrade according to the upgrade policy
    }

    // Helper function to populate table Shop
    private void insertShops(SQLiteDatabase db) {
        for (String shop : SHOP_DATA) {
            ContentValues values = new ContentValues();
            values.put(shoppingListContract.Shop.COLUMN_NAME_NAME, shop);
            db.insert(shoppingListContract.Shop.TABLE_NAME, null, values);
        }
    }

    // Helper function to populate table Category
    private void insertCategory(SQLiteDatabase db) {
        for (int i = 0; i < CATEGORY_DATA.length; i++) {
            ContentValues values = new ContentValues();
            values.put(shoppingListContract.Category.COLUMN_NAME_NAME, CATEGORY_DATA[i]);
            categoryIDs[i] = db.insert(shoppingListContract.Category.TABLE_NAME, null, values);
        }
    }

    // Helper function to populate table Unit
    private void insertUnit(SQLiteDatabase db) {
        for (String unit : UNIT_DATA) {
            ContentValues values = new ContentValues();
            values.put(shoppingListContract.Unit.COLUMN_NAME_NAME, unit);
            db.insert(shoppingListContract.Unit.TABLE_NAME, null, values);
        }
    }

    // Helper function to populate table List with a default list
    private void insertList(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(shoppingListContract.List.COLUMN_NAME_NAME, "Mijn boodschappenlijst");
        values.put(shoppingListContract.List.COLUMN_NAME_IS_RECIPE, 0);
        db.insert(shoppingListContract.List.TABLE_NAME, null, values);
    }

    // Helper function to populate table Product
    private void insertProduct(SQLiteDatabase db) {
        for (int i = 0; i < CATEGORY_DATA.length; i++) {
            for (String product : PRODUCT_DATA[i]) {
                ContentValues values = new ContentValues();
                values.put(shoppingListContract.Product.COLUMN_NAME_CATEGORY_ID, categoryIDs[i]);
                values.put(shoppingListContract.Product.COLUMN_NAME_NAME, product);
            }
        }
    }
}
