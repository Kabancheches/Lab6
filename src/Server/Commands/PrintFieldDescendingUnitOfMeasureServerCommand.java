package Server.Commands;

import Common.Model.Classes.Product;
import Common.Model.Enums.UnitOfMeasure;
import Server.Managers.CollectionManager;
import Server.Managers.FileManager;
import Common.Net.CommandRequest;
import Common.Net.CommandResponse;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class PrintFieldDescendingUnitOfMeasureServerCommand implements ServerCommand {
    @Override
    public CommandResponse execute(CommandRequest request, CollectionManager collectionManager, FileManager fm) {
        Set<UnitOfMeasure> units = new TreeSet<>(Comparator.reverseOrder());
        for (Product p : collectionManager.getCollection()) {
            if (p.getUnitOfMeasure() != null) {
                units.add(p.getUnitOfMeasure());
            }
        }
        if (units.isEmpty()) {
            return new CommandResponse(true, "Нет данных о единицах измерения.");
        }
        StringBuilder sb = new StringBuilder("Единицы измерения в порядке убывания:\n");
        for (UnitOfMeasure u : units) {
            sb.append(u.name()).append("\n");
        }
        return new CommandResponse(true, sb.toString());
    }
}