package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.ParameterizedType;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.MobEffectList;

public class _ENUMARG<E> implements ArgumentType<E> {

	@Override
	public <S> E parse(StringReader reader) throws CommandSyntaxException {

		int cursor = reader.getCursor();

		while (reader.canRead() && canPeak(reader.peek())) {
			reader.skip();
		}

		String resultantString = reader.getString().substring(cursor, reader.getCursor());

		ParameterizedType a = (ParameterizedType) getClass().getGenericSuperclass();
		
		Enum.valueOf((Class<T>) a.getActualTypeArguments()[0].getClass(), resultantString);
		return E.class;
	}
	
	public static boolean canPeak(char arg) {
		return 	arg >= 48 && arg <= 57 || //0 - 9
				arg >= 97 && arg <= 122 || //a - z
				arg == 95 || //_ (underscore)
				arg == 58 || //: (colon)
				arg == 47 || /// (forward slash)
				arg == 46 || //. (period)
				arg == 45;	 //- (dash)
	}
	
//	@Override
//	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> arg0, SuggestionsBuilder arg1) {
//		return ICompletionProvider.a(MobEffectList.REGISTRY.keySet(), arg1);
//
//		//return Suggestions.empty();
//	}
	



	
	
	
}
