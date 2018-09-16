package com.glandroid.addcontact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private EditText et_name;
    private EditText et_email;
    private EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        performCodeWithPermission("插入联系人信息", new PermissionCallback() {
            @Override
            public void hasPermission() {

            }

            @Override
            public void noPermission() {

            }
        }, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS);
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);
    }

    /**
     * 添加联系人信息
     *
     * @param view
     */
    public void addContact(View view) {
        String name = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        // 1.在raw_contact表里面添加联系人id
        Cursor cursor = resolver.query(uri, null, null, null, null, null);
        int new_id = cursor.getCount() + 1;

        ContentValues values = new ContentValues();
        values.put("contact_id", new_id);
        resolver.insert(uri, values);

        // 2.在data表里面添加联系人的数据

        ContentValues namevalues = new ContentValues();
        namevalues.put("raw_contact_id", new_id);
        namevalues.put("data1", name);
        namevalues.put("mimetype", "vnd.android.cursor.item/name");
        resolver.insert(dataUri, namevalues);

        ContentValues phonevalues = new ContentValues();
        phonevalues.put("raw_contact_id", new_id);
        phonevalues.put("data1", phone);
        phonevalues.put("mimetype", "vnd.android.cursor.item/phone_v2");
        resolver.insert(dataUri, phonevalues);

        ContentValues emailvalues = new ContentValues();
        emailvalues.put("raw_contact_id", new_id);
        emailvalues.put("data1", email);
        emailvalues.put("mimetype", "vnd.android.cursor.item/email_v2");
        resolver.insert(dataUri, emailvalues);

        Toast.makeText(this, "添加信息成功", Toast.LENGTH_SHORT).show();
    }
}
