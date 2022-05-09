package com.ahmadabuhasan.pointofsale.settings.backup;

import android.os.Environment;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.ahmadabuhasan.pointofsale.R;
import com.ahmadabuhasan.pointofsale.database.DatabaseOpenHelper;

import java.io.File;

public class LocalBackup {

    private BackupActivity activity;

    public LocalBackup(BackupActivity backupActivity) {
        this.activity = backupActivity;
    }

    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    public void performBackup(final DatabaseOpenHelper db, final String outFileName) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));

        boolean success = true;
        if (!folder.exists())
            success = folder.mkdirs();
        if (success) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
            builder.setTitle(R.string.database_backup);
            builder.setCancelable(false);
            final EditText input = new EditText(this.activity);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setMessage(R.string.enter_local_database_backup_name)
                    .setPositiveButton(R.string.yes, (dialogInterface, which) -> {
                        String m_text = input.getText().toString();
                        String out = outFileName + m_text + ".db";
                        db.backup(out);
                    });
            builder.setNegativeButton(R.string.no, (dialogInterface, which) -> dialogInterface.cancel());

            builder.show();
        } else
            Toast.makeText(this.activity, R.string.unable_to_create_directory_retry, Toast.LENGTH_SHORT).show();
    }

    //ask to the user what backup to restore
    public void performRestore(final DatabaseOpenHelper db) {

        Permissions.verifyStoragePermissions(activity);

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name));
        if (folder.exists()) {

            final File[] files = folder.listFiles();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.activity, android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this.activity);
            builderSingle.setTitle(R.string.database_restore);
            builderSingle.setNegativeButton(R.string.cancel,
                    (dialogInterface, which) -> dialogInterface.dismiss());
            builderSingle.setAdapter(arrayAdapter,
                    (dialogInterface, which) -> {
                        try {
                            db.importDB(files[which].getPath());
                        } catch (Exception e) {
                            Toast.makeText(this.activity, R.string.unable_to_restore_retry, Toast.LENGTH_SHORT).show();
                        }
                    });
            builderSingle.show();
        } else
            Toast.makeText(this.activity, R.string.backup_folder_not_present, Toast.LENGTH_SHORT).show();
    }
    // https://github.com/prof18/Database-Backup-Restore.git
}